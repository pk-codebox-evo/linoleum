package linoleum;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import java.util.concurrent.CountDownLatch;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import linoleum.application.Frame;
import linoleum.application.OptionPanel;
import linoleum.application.event.ClassPathChangeEvent;

public class ScriptShell extends Frame implements ScriptShellPanel.CommandProcessor {
	private final Preferences prefs = Preferences.userNodeForPackage(getClass());
	private final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	private final CountDownLatch engineReady = new CountDownLatch(1);
	private final Packages pkgs = Desktop.instance.getPackages();
	private final URI uri = new File(".").toURI();
	private volatile ScriptEngine engine;
	private volatile String prompt;
	private	ScriptEngineManager manager;
	private String extension;

	public ScriptShell() {
		this(null);
	}

	public ScriptShell(final Frame parent) {
		super(parent);
		initComponents();
		setIcon(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/development/Host24.gif")));
		createScriptEngine();
		setTitle(engine.getFactory().getLanguageName());
		setContentPane(new ScriptShellPanel(this));
	}

	private void refresh() {
		model.removeAllElements();
		manager = new ScriptEngineManager();
		for (final ScriptEngineFactory sef : manager.getEngineFactories()) {
			model.addElement(sef.getEngineName());
		}
	}

	@Override
	public void classPathChanged(final ClassPathChangeEvent e) {
		refresh();
	}

	@Override
	public OptionPanel getOptionPanel() {
		return optionPanel1;
	}

	@Override
	public void load() {
		model.setSelectedItem(prefs.get(getName() + ".language", ""));
	}

	@Override
	public void save() {
		prefs.put(getName() + ".language", (String) model.getSelectedItem());
	}

	@Override
	public void open() {
		(new Thread() {
			@Override
			public void run() {
				initScriptEngine();
				engineReady.countDown();
			}
		}).start();
	}

	@Override
	public URI getURI() {
		return uri;
	}

	@Override
	public String getPrompt() {
		return prompt;
	}

	@Override
	public String executeCommand(String cmd) {
		String res;
		try {
			engineReady.await();
			Object tmp = engine.eval(cmd);
			res = (tmp == null) ? null : tmp.toString();
		} catch (InterruptedException ie) {
			res = ie.getMessage();
		} catch (ScriptException se) {
			res = se.getMessage();
		}
		return res;
	}

	@Override
	public Frame getFrame(final Frame parent) {
		return new ScriptShell(parent);
	}

	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                optionPanel1 = new linoleum.application.OptionPanel();
                jLabel2 = new javax.swing.JLabel();
                jComboBox1 = new javax.swing.JComboBox();

                optionPanel1.setFrame(this);

                jLabel2.setText("Language :");

                jComboBox1.setModel(model);
                jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBox1ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout optionPanel1Layout = new javax.swing.GroupLayout(optionPanel1);
                optionPanel1.setLayout(optionPanel1Layout);
                optionPanel1Layout.setHorizontalGroup(
                        optionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, 0, 294, Short.MAX_VALUE)
                                .addContainerGap())
                );
                optionPanel1Layout.setVerticalGroup(
                        optionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(optionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                setClosable(true);
                setIconifiable(true);
                setMaximizable(true);
                setResizable(true);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 394, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 277, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
		optionPanel1.setDirty(true);
        }//GEN-LAST:event_jComboBox1ActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JLabel jLabel2;
        private linoleum.application.OptionPanel optionPanel1;
        // End of variables declaration//GEN-END:variables

	private void createScriptEngine() {
		final String language = prefs.get(getName() + ".language", "");
		refresh();
		for (final ScriptEngineFactory sef : manager.getEngineFactories()) {
			if (engine == null || language.equals(sef.getEngineName())) {
				engine = sef.getScriptEngine();
			}
		}
		if (engine == null) {
			throw new RuntimeException("cannot load " + language + " engine");
		}
		extension = engine.getFactory().getExtensions().get(0);
		prompt = extension + ">";
	}

	// create and initialize script engine
	private void initScriptEngine() {
		// set pre-defined global variables
		setGlobals();
		// load pre-defined initialization file
		loadInitFile(ClassLoader.getSystemResource("com/sun/tools/script/shell/init." + extension));
		final File home = pkgs.home;
		loadUserInitFile(new File(home, "init." + extension));
		try {
			if (!Files.isSameFile(home.toPath(), Paths.get("."))) {
				// load current user's initialization file
				loadUserInitFile(new File("init." + extension));
			}
		} catch (final IOException e) {}
	}

	// set pre-defined global variables for script
	private void setGlobals() {
		engine.put("engine", engine);
		engine.put("frame", this);
	}

	private void loadInitFile(final URL url) {
		String oldFilename = (String) engine.get(ScriptEngine.FILENAME);
		if (url == null) {
			return;
		}
		engine.put(ScriptEngine.FILENAME, url.getPath());
		try (final InputStreamReader reader = new InputStreamReader(url.openStream())) {
			engine.eval(reader);
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			engine.put(ScriptEngine.FILENAME, oldFilename);
		}
	}

	private void loadUserInitFile(final File file) {
		final String oldFilename = (String) engine.get(ScriptEngine.FILENAME);
		if (!file.exists()) {
			return;
		}
		engine.put(ScriptEngine.FILENAME, file.getName());
		try (final InputStreamReader reader = new FileReader(file)) {
			engine.eval(reader);
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			engine.put(ScriptEngine.FILENAME, oldFilename);
		}
	}
}
