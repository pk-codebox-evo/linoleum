package linoleum.application;

import java.awt.Component;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import javax.activation.FileTypeMap;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ApplicationManager extends javax.swing.JInternalFrame {
	private final ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/toolbarButtonGraphics/development/Application24.gif"));
	private final Map<String, Application> map = new HashMap<String, Application>();
	private final Map<String, String> apps = new HashMap<String, String>();
	private final Map<String, ImageIcon> icons = new HashMap<String, ImageIcon>();
	private final DefaultListModel model = new DefaultListModel();
	private final ListCellRenderer renderer = new Renderer();

	private class Renderer extends JLabel implements ListCellRenderer {

		public Renderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
			setVerticalTextPosition(BOTTOM);
			setHorizontalTextPosition(CENTER);
		}

		@Override
		public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			final String name = (String)value;
			setIcon(icons.get(name));
			setText(name);
			setFont(list.getFont());

			return this;
		}
	};

	public ApplicationManager() {
		initComponents();
		refresh();
	}

	public void open(final URI uri) {
		final File file = Paths.get(uri).toFile();
		final String str = FileTypeMap.getDefaultFileTypeMap().getContentType(file);
		if (apps.containsKey(str)) {
			open(apps.get(str), uri);
			return;
		}
		try {
			final MimeType type = new MimeType(str);
			for (final Map.Entry<String, String> entry : apps.entrySet()) {
				final String key = entry.getKey();
				final String value = entry.getValue();
				if (type.match(key)) {
					open(value, uri);
					return;
				}
			}
		} catch (final MimeTypeParseException ex) {}
		if (file.isDirectory()) {
			open("FileManager", uri);
		}
	}

	private void open(final String name, final URI uri) {
		if (map.containsKey(name)) {
			final JInternalFrame frame = map.get(name).open(uri);
			if (frame.getDesktopPane() == null) getDesktopPane().add(frame);
			frame.setVisible(true);
		}
	}

	private void open(final int index) {
		open((String)model.getElementAt(index), null);
	}

	public final void refresh() {
		final ServiceLoader<Application> loader = ServiceLoader.load(Application.class);
		for (final Application app : loader) {
			final String name = app.getName();
			if (!map.containsKey(name)) {
				map.put(name, app);
				final ImageIcon icon = app.getIcon();
				icons.put(name, icon == null?defaultIcon:icon);
				final String type = app.getMimeType();
				if (type != null) for (final String s : type.split(":")) apps.put(s, name);
				model.addElement(name);
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jScrollPane1 = new javax.swing.JScrollPane();
                jList1 = new javax.swing.JList();

                setClosable(true);
                setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
                setIconifiable(true);
                setMaximizable(true);
                setResizable(true);
                setTitle("Applications");

                jList1.setModel(model);
                jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                jList1.setCellRenderer(renderer);
                jList1.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
                jList1.setVisibleRowCount(-1);
                jList1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                jList1MouseClicked(evt);
                        }
                });
                jScrollPane1.setViewportView(jList1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
		if (evt.getClickCount() == 2) {
			open(jList1.locationToIndex(evt.getPoint()));
		}
        }//GEN-LAST:event_jList1MouseClicked


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JList jList1;
        private javax.swing.JScrollPane jScrollPane1;
        // End of variables declaration//GEN-END:variables
}
