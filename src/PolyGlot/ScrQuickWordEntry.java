/*
 * Copyright (c) 2014, draque
 * All rights reserved.
 *
 * Licensed under: Creative Commons Attribution-NonCommercial 4.0 International Public License
 *  See LICENSE.TXT included with this code to read the full license agreement.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package PolyGlot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

/**
 *
 * @author draque
 */
public class ScrQuickWordEntry extends PDialog {

    private final KeyListener enterListener;
    private final DictCore core;
    private final String cstSELET = "";

    /**
     * Creates new form scrQuickWordEntry
     *
     * @param _core Dictionary core
     */
    public ScrQuickWordEntry(DictCore _core) {
        core = _core;

        initComponents();

        enterListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // User only wants to enter word if no popups are visible
                if (e.getKeyCode() == KeyEvent.VK_ENTER
                        && (!cmbGender.isPopupVisible())
                        && !cmbType.isPopupVisible()) {
                    tryRecord();
                }
            }

            @Override public void keyReleased(KeyEvent e) {/*DO NOTHING*/}
            @Override public void keyTyped(KeyEvent e) { /*DO NOTHING*/}
        };

        txtConWord.setFont(core.getFontCon());
        txtConWord.addKeyListener(enterListener);
        txtDefinition.addKeyListener(enterListener);
        txtLocalWord.addKeyListener(enterListener);
        txtProc.addKeyListener(enterListener);
        cmbGender.addKeyListener(enterListener);
        cmbType.addKeyListener(enterListener);

        // conword is always required and is initially selected
        txtConWord.setBackground(core.getRequiredColor());
        txtConWord.requestFocus();

        if (core.getPropertiesManager().isLocalMandatory()) {
            txtLocalWord.setBackground(core.getRequiredColor());
            chkLocal.setEnabled(false);
        }
        if (core.getPropertiesManager().isTypesMandatory()) {
            cmbType.setForeground(core.getRequiredColor());
            chkType.setEnabled(false);
        }
        
        populateTypes();
        populateGenders();
    }
    
    private void populateTypes() {
        Iterator<TypeNode> typeIt = core.getTypes().getNodeIterator();
        cmbType.removeAllItems();
        cmbType.addItem(cstSELET);
        while (typeIt.hasNext()) {
            TypeNode curType = typeIt.next();
            cmbType.addItem(curType);
        }
    }

    private void populateGenders() {
        Iterator<GenderNode> gendIt = core.getGenders().getNodeIterator();
        cmbGender.removeAllItems();
        cmbGender.addItem(cstSELET);
        while (gendIt.hasNext()) {
            GenderNode curGen = gendIt.next();
            cmbGender.addItem(curGen);
        }
    }
    
    /**
     * records a word if appropriate, flashes required fields otherwise
     */
    private void tryRecord() {
        ConWord word = new ConWord();
        boolean success = true;
        
        // conword is always mandatory
        if (txtConWord.getText().equals("")) {
            PGTools.flashComponent(txtConWord, core.getRequiredColor(), true);
            success = false;
        }
        
        // test for mandatory local word
        if (core.getPropertiesManager().isLocalMandatory()
                && txtLocalWord.getText().equals("")) {
            PGTools.flashComponent(txtLocalWord, core.getRequiredColor(), true);
            success = false;
        }
        
        // test for mandatory types
        if (core.getPropertiesManager().isTypesMandatory()
                && cmbType.getSelectedItem().toString().equals(cstSELET)) {
            PGTools.flashComponent(cmbType, core.getRequiredColor(), false);
            success = false;
        }
        
        // test for con word uniqueness
        if (!txtConWord.getText().equals("")
                &&core.getPropertiesManager().isWordUniqueness()) {
            word.setValue(txtConWord.getText());
            try {
                if (core.filteredWordList(word).hasNext()) {
                    throw new Exception("Conword \"" + word.getValue() + "\" already exists.");
                }
            } catch (Exception e) {
                InfoBox.error("Quickinsert Error", "Unable to quickinsert: "+ e.getMessage(), this);
                success = false;
            }
            word = new ConWord();
        }
        
        // test for local word uniqueness
        if (!txtLocalWord.getText().equals("")
                && core.getPropertiesManager().isLocalUniqueness()) {
            word.setLocalWord(txtLocalWord.getText());
            try {
                if (core.filteredWordList(word).hasNext()) {
                    throw new Exception("Local word \"" + word.getValue() + "\" already exists.");
                }
            } catch (Exception e) {
                InfoBox.error("Quickinsert Error", "Unable to quickinsert: "+ e.getMessage(), this);
                success = false;
            }
            word = new ConWord();
        }
        
        if (!success) {
            return;
        }
        
        try {
            word.setValue(txtConWord.getText());
            word.setDefinition(txtDefinition.getText());
            word.setLocalWord(txtLocalWord.getText());
            word.setPronunciation(txtProc.getText());
            word.setGender(cmbGender.getSelectedItem().toString());
            word.setWordType(cmbType.getSelectedItem().toString());
            
            core.addWord(word);
            blankWord();
            txtConWord.requestFocus();
        } catch (Exception e) {
            InfoBox.error("Word Error", "Unable to insert word: " + e.getMessage(), this);
        }
    }
    
    /**
     * blanks out current conword fields
     */
    private void blankWord() {
        txtConWord.setText("");
        txtDefinition.setText("");
        txtLocalWord.setText("");
        txtProc.setText("");
        cmbGender.setSelectedIndex(0);
        cmbType.setSelectedIndex(0);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        chkLocal = new javax.swing.JCheckBox();
        chkType = new javax.swing.JCheckBox();
        chkGender = new javax.swing.JCheckBox();
        chkProc = new javax.swing.JCheckBox();
        chkDefinition = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        txtConWord = new javax.swing.JTextField();
        txtLocalWord = new javax.swing.JTextField();
        cmbType = new javax.swing.JComboBox();
        cmbGender = new javax.swing.JComboBox();
        txtProc = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDefinition = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnDone = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Word Quickentry");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        chkLocal.setSelected(true);
        chkLocal.setText("Local Word");
        chkLocal.setToolTipText("Enable/Disable Local Word Entry");
        chkLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLocalActionPerformed(evt);
            }
        });

        chkType.setSelected(true);
        chkType.setText("Type");
        chkType.setToolTipText("Enable/Disable Type Entry");
        chkType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTypeActionPerformed(evt);
            }
        });

        chkGender.setSelected(true);
        chkGender.setText("Gender");
        chkGender.setToolTipText("Enable/Disable Gender Entry");
        chkGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGenderActionPerformed(evt);
            }
        });

        chkProc.setSelected(true);
        chkProc.setText("Pronunciation");
        chkProc.setToolTipText("Enable/Disable Pronunciation Entry");
        chkProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkProcActionPerformed(evt);
            }
        });

        chkDefinition.setSelected(true);
        chkDefinition.setText("Definition");
        chkDefinition.setToolTipText("Enable/Disable Definition Entry");
        chkDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDefinitionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkLocal)
                    .addComponent(chkType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chkGender)
                        .addGap(40, 40, 40)
                        .addComponent(chkDefinition))
                    .addComponent(chkProc))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkLocal)
                    .addComponent(chkGender)
                    .addComponent(chkDefinition))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkType)
                    .addComponent(chkProc))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Definition");

        txtDefinition.setColumns(20);
        txtDefinition.setLineWrap(true);
        txtDefinition.setRows(5);
        txtDefinition.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtDefinition);

        jLabel2.setText("Con Word");

        jLabel3.setText("Local Word");

        jLabel4.setText("Type");

        jLabel5.setText("Gender");

        jLabel6.setText("Pronunciation");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProc))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(49, 49, 49)
                        .addComponent(cmbGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(63, 63, 63)
                        .addComponent(cmbType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(24, 24, 24)
                        .addComponent(txtLocalWord))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(32, 32, 32)
                        .addComponent(txtConWord)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtConWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLocalWord, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnDone.setText("Done");
        btnDone.setToolTipText("Exit quickentry window");
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 312, Short.MAX_VALUE)
                .addComponent(btnDone))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDone))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        dispose();
    }//GEN-LAST:event_btnDoneActionPerformed

    private void chkLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLocalActionPerformed
        txtLocalWord.setEnabled(chkLocal.isSelected());
    }//GEN-LAST:event_chkLocalActionPerformed

    private void chkGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGenderActionPerformed
        cmbGender.setEnabled(chkGender.isSelected());
    }//GEN-LAST:event_chkGenderActionPerformed

    private void chkDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDefinitionActionPerformed
        txtDefinition.setEnabled(chkDefinition.isSelected());
    }//GEN-LAST:event_chkDefinitionActionPerformed

    private void chkTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTypeActionPerformed
        cmbType.setEnabled(chkType.isSelected());
    }//GEN-LAST:event_chkTypeActionPerformed

    private void chkProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkProcActionPerformed
        txtProc.setEnabled(chkProc.isSelected());
    }//GEN-LAST:event_chkProcActionPerformed

    /**
     * @param _core Dictionary Core
     * @return created window
     */
    public static ScrQuickWordEntry run(DictCore _core) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScrQuickWordEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        ScrQuickWordEntry ret = new ScrQuickWordEntry(_core);
        ret.setModal(true);
        ret.setVisible(true);
        return ret;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JCheckBox chkDefinition;
    private javax.swing.JCheckBox chkGender;
    private javax.swing.JCheckBox chkLocal;
    private javax.swing.JCheckBox chkProc;
    private javax.swing.JCheckBox chkType;
    private javax.swing.JComboBox cmbGender;
    private javax.swing.JComboBox cmbType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtConWord;
    private javax.swing.JTextArea txtDefinition;
    private javax.swing.JTextField txtLocalWord;
    private javax.swing.JTextField txtProc;
    // End of variables declaration//GEN-END:variables
}
