package main.user_interface;

import java.awt.AWTEvent;

import javax.swing.JButton;

import main.system.AHHASCSystem;

public class CentreManagerPanel extends javax.swing.JPanel {
        private AHHASCSystem system;
        private UserInterface userInterface;
        private CentreManagerCard centreManagerCard;

        /**
         * Creates new form centreManagerPanel
         */
        public CentreManagerPanel(CentreManagerCard centreManagerCard, UserInterface userInterface,
                        AHHASCSystem system) {
                this.system = system;
                this.centreManagerCard = centreManagerCard;
                initComponents();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

                jLabel1 = new javax.swing.JLabel();
                jButton1 = new javax.swing.JButton();
                jButton2 = new javax.swing.JButton();
                jButton4 = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();
                jButton5 = new javax.swing.JButton();

                jLabel1.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
                jLabel1.setText("AHHASC Manager Page");

                jButton1.setText("Edit Profile");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                editProfileActionPerformed(evt);
                        }
                });

                JButton jButton2 = new LogoutButton(userInterface, system).getLogoutButton();

                jButton4.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
                jButton4.setText("Manage Users");
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                manageUsersActionPerformed(evt);
                        }
                });

                jButton3.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
                jButton3.setText("Manage Appointments");
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                manageAppointmentsActionPerformed(evt);
                        }
                });

                jButton5.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
                jButton5.setText("Manage Customers");
                jButton5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                manageCustomersActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jButton2)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jButton1)
                                                                .addContainerGap())
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(86, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                .addComponent(jLabel1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                295,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                .addComponent(jButton4,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                272,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(35, 35, 35)))
                                                                                .addGroup(layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                false)
                                                                                                .addComponent(jButton3,
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(jButton5,
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)))
                                                                .addGap(44, 44, 44)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jButton1)
                                                                                .addComponent(jButton2))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                73,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                34,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton5,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                34,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                33,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(74, Short.MAX_VALUE)));
        }// </editor-fold>

        private void editProfileActionPerformed(java.awt.event.ActionEvent evt) {
                // TODO add your handling code here:
                // TODO add this button (and logout) for technicians
        }

        private void manageUsersActionPerformed(java.awt.event.ActionEvent evt) {
                centreManagerCard.showPanel("userManagement");
        }

        private void manageCustomersActionPerformed(java.awt.event.ActionEvent evt) {
                centreManagerCard.showPanel("customerManagement");
        }

        private void manageAppointmentsActionPerformed(java.awt.event.ActionEvent evt) {
                centreManagerCard.showPanel("appointments");
        }

        // Variables declaration - do not modify
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JButton jButton5;
        private javax.swing.JLabel jLabel1;
        // End of variables declaration
}
