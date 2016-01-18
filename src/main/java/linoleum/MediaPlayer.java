package linoleum;

import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.activation.FileTypeMap;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.Player;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

public class MediaPlayer extends JInternalFrame {
	private Player player;
	private static final String audio = "audio/*";
	private static final String video = "video/*";
	private final ImageIcon playIcon = new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"));
	private final ImageIcon pauseIcon = new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Pause16.gif"));
	private final File files[];
	private boolean state;
	private int index;

	public static class Application implements linoleum.application.Application {
		public String getName() {
			return MediaPlayer.class.getSimpleName();
		}

		public ImageIcon getIcon() {
			return new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Movie24.gif"));
		}

		public String getMimeType() {
			return audio + ":" + video;
		}

		public JInternalFrame open(final URI uri) {
			return new MediaPlayer(uri == null?null:Paths.get(uri).toFile());
		}
	}

	public MediaPlayer(final File file) {
		initComponents();
		if (file != null) {
			files = file.getParentFile().listFiles(new FileFilter() {
				public boolean accept(final File file) {
					return canOpen(file);
				}
			});
			Arrays.sort(files);
			index = Arrays.binarySearch(files, file);
		} else {
			files = new File[] {};
		}
		play();
	}

	private static boolean canOpen(final File file) {
		final String str = FileTypeMap.getDefaultFileTypeMap().getContentType(file);
		try {
			final MimeType type = new MimeType(str);
			return type.match(audio) || type.match(video);
		} catch (final MimeTypeParseException ex) {}
		return false;
	}

	private void play() {
		open();
		if (player != null) {
			pause();
		}
	}

	private void open() {
		if (index < files.length) {
			final File file = files[index];
			setTitle(file.getName());
			try {
				player = Manager.createRealizedPlayer(file.toURI().toURL());
				final Component component = player.getVisualComponent();
				if (component != null) {
					jPanel1.removeAll();
					jPanel1.add(component);
					pack();
				}
				player.addControllerListener(new ControllerListener() {
					
					@Override
					public void controllerUpdate(ControllerEvent ce) {
						if (ce instanceof EndOfMediaEvent) {
							stop();
							index++;
							if (index < files.length) play();
							else index = 0;
						}
					}
				});
			} catch (final Exception ex) {
				player = null;
			}
		}
	}

	private void stop() {
		if (player != null) {
			player.close();
			state = false;
			jButton1.setIcon(playIcon);
			player = null;
		}
	}

	private void pause() {
		if (state) {
			state = false;
			player.stop();
			jButton1.setIcon(playIcon);
		} else {
			state = true;
			player.start();
			jButton1.setIcon(pauseIcon);
		}
	}

	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jButton2 = new javax.swing.JButton();
                jButton4 = new javax.swing.JButton();
                jButton1 = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();

                setClosable(true);
                setIconifiable(true);
                setMaximizable(true);
                setResizable(true);
                setTitle("Media Player");
                addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
                        public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                        }
                        public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                                formInternalFrameClosing(evt);
                        }
                        public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                        }
                        public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                        }
                        public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                        }
                        public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                        }
                        public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
                        }
                });

                jPanel1.setLayout(new java.awt.BorderLayout());
                getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

                jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/StepBack16.gif"))); // NOI18N
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton2ActionPerformed(evt);
                        }
                });
                jPanel2.add(jButton2);

                jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Stop16.gif"))); // NOI18N
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton4ActionPerformed(evt);
                        }
                });
                jPanel2.add(jButton4);

                jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });
                jPanel2.add(jButton1);

                jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/StepForward16.gif"))); // NOI18N
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });
                jPanel2.add(jButton3);

                getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		if (player == null) {
			play();
		} else {
			pause();
		}
        }//GEN-LAST:event_jButton1ActionPerformed

        private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
		if (player != null) player.close();
        }//GEN-LAST:event_formInternalFrameClosing

        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		stop();
		if (files.length > 0) index = (index + 1) % files.length;
		play();
        }//GEN-LAST:event_jButton3ActionPerformed

        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		stop();
		if (files.length > 0) index = (index - 1 + files.length) % files.length;
		play();
        }//GEN-LAST:event_jButton2ActionPerformed

        private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		stop();
        }//GEN-LAST:event_jButton4ActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        // End of variables declaration//GEN-END:variables
}
