package linoleum.mail;

import java.awt.Component;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

public class Settings extends javax.swing.JPanel {
	private final Preferences prefs = Preferences.userNodeForPackage(getClass());

	public Settings() {
		initComponents();
	}

	public void show(final Component parent) {
		jTextField1.setText(prefs.get(SimpleClient.name + ".url", null));
		jTextField2.setText(prefs.get(SimpleClient.name + ".mailhost", null));
		jTextField3.setText(prefs.get(SimpleClient.name + ".from", null));
		jTextField4.setText(prefs.get(SimpleClient.name + ".record", "Sent"));
		jCheckBox1.setSelected(prefs.getBoolean(SimpleClient.name + ".debug", false));
		final int result = JOptionPane.showInternalConfirmDialog(parent, this, "Account settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			prefs.put(SimpleClient.name + ".url", jTextField1.getText());
			prefs.put(SimpleClient.name + ".mailhost", jTextField2.getText());
			prefs.put(SimpleClient.name + ".from", jTextField3.getText());
			prefs.put(SimpleClient.name + ".record", jTextField4.getText());
			prefs.putBoolean(SimpleClient.name + ".debug", jCheckBox1.isSelected());
		}
	}
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabel1 = new javax.swing.JLabel();
                jTextField1 = new javax.swing.JTextField();
                jLabel2 = new javax.swing.JLabel();
                jTextField2 = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                jTextField3 = new javax.swing.JTextField();
                jLabel4 = new javax.swing.JLabel();
                jTextField4 = new javax.swing.JTextField();
                jCheckBox1 = new javax.swing.JCheckBox();

                jLabel1.setText("URL :");

                jLabel2.setText("Mailhost :");

                jLabel3.setText("From :");

                jLabel4.setText("Record :");

                jCheckBox1.setText("Debug");

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jCheckBox1)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jTextField4)
                                        .addComponent(jTextField3)
                                        .addComponent(jTextField1)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
        }// </editor-fold>//GEN-END:initComponents


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
        private javax.swing.JTextField jTextField3;
        private javax.swing.JTextField jTextField4;
        // End of variables declaration//GEN-END:variables
}
