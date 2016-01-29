package linoleum;

import linoleum.application.Frame;

public class Clock extends Frame {

	public Clock() {
		initComponents();
		setSingle(true);
	}

	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                clockPanel1 = new linoleum.ClockPanel();

                setClosable(true);
                setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
                setIconifiable(true);
                setMaximizable(true);
                setResizable(true);
                setTitle("Clock");
                addComponentListener(new java.awt.event.ComponentAdapter() {
                        public void componentShown(java.awt.event.ComponentEvent evt) {
                                formComponentShown(evt);
                        }
                        public void componentHidden(java.awt.event.ComponentEvent evt) {
                                formComponentHidden(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(clockPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(clockPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
		clockPanel1.stop();
        }//GEN-LAST:event_formComponentHidden

        private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		clockPanel1.start();
        }//GEN-LAST:event_formComponentShown

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private linoleum.ClockPanel clockPanel1;
        // End of variables declaration//GEN-END:variables
}
