
package net.sf.freecol.client.gui.panel;


import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.Monarch;

import cz.autel.dmi.HIGLayout;

/**
 * This panel is used to show information about a tile.
 */
public final class MonarchPanel extends FreeColDialog implements ActionListener {

    public static final String  COPYRIGHT = "Copyright (C) 2003-2006 The FreeCol Team";
    public static final String  LICENSE = "http://www.gnu.org/licenses/gpl.html";
    public static final String  REVISION = "$Revision$";

    private static final Logger logger = Logger.getLogger(MonarchPanel.class.getName());

    private static final int OK = 0;
    private static final int CANCEL = 1;
    private final Canvas canvas;

    private final JLabel header;
    private final JLabel imageLabel;

    private JTextArea textArea;
    private final JButton okButton;
    private final JButton cancelButton;
    private JPanel buttonPanel;
    
    /**
    * The constructor that will add the items to this panel.
    * @param parent The parent panel.
    */
    public MonarchPanel(Canvas parent) {
        canvas = parent;

        header = new JLabel("", SwingConstants.CENTER);
        header.setFont(((Font) UIManager.get("HeaderFont")).deriveFont(0, 36));
        header.setText(Messages.message("aMessageFromTheCrown"));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageLabel = new JLabel();
        Image image = (Image) UIManager.get("MonarchImage");
        imageLabel.setIcon(new ImageIcon(image));

        okButton = new JButton(Messages.message("ok"));
        okButton.setActionCommand(String.valueOf(OK));
        okButton.addActionListener(this);
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        cancelButton = new JButton();
        cancelButton.setActionCommand(String.valueOf(CANCEL));
        cancelButton.addActionListener(this);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    }


    public void requestFocus() {
        okButton.requestFocus();
    }


    /**
    * Initializes the information that is being displayed on this panel.
    * The information displayed will be based on the monarch action.
    *
    * @param action The monarch action.
    * @param replace The data to be used when displaying i18n-strings.
    */
    public void initialize(int action, String[][] replace) {

        int[] widths = {0, margin, 0};
        int[] heights = {0, margin, 0, margin, 0};
        setLayout(new HIGLayout(widths, heights));
        
        int row = 1;
        int imageColumn = 1;
        int textColumn = 3;
        add(header, higConst.rcwh(row, imageColumn, widths.length, 1));
        row += 2;

        add(imageLabel, higConst.rc(row, imageColumn));

        String messageID;
        String okText = "ok";
        String cancelText = null;
        switch (action) {
        case Monarch.RAISE_TAX:
            messageID = "model.monarch.raiseTax";
            okText = "model.monarch.acceptTax";
            cancelText = "model.monarch.rejectTax";
            break;
        case Monarch.ADD_TO_REF:
            messageID = "model.monarch.addToREF";
            break;
        case Monarch.DECLARE_WAR:
            messageID = "model.monarch.declareWar";
            break;
        case Monarch.SUPPORT_SEA:
            messageID = "model.monarch.supportSea";
            cancelText = "display";
            break;
        case Monarch.SUPPORT_LAND:
            messageID = "model.monarch.supportLand";
            cancelText = "display";
            break;
        case Monarch.WAIVE_TAX:
            messageID = "model.monarch.waiveTax";
            break;
        case Monarch.OFFER_MERCENARIES:
            messageID = "model.monarch.offerMercenaries";
            okText = "model.monarch.acceptMercenaries";
            cancelText = "model.monarch.rejectMercenaries";
            break;
        default:
            messageID = "Unknown monarch action: " + action;
        }

        textArea = getDefaultTextArea(Messages.message(messageID, replace));
        add(textArea, higConst.rc(row, textColumn));
        row += 2;

        buttonPanel = new JPanel();
        okButton.setText(Messages.message(okText));
        buttonPanel.add(okButton);
        if (cancelText != null) {
            cancelButton.setText(Messages.message(cancelText));
            buttonPanel.add(cancelButton);
        }
        add(buttonPanel, higConst.rcwh(row, imageColumn, widths.length, 1));
        
        setSize(getPreferredSize());
    }


    /**
    * This function analyses an event and calls the right methods to take
    * care of the user's requests.
    * @param event The incoming ActionEvent.
    */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            switch (Integer.valueOf(command).intValue()) {
            case OK:
                setResponse(new Boolean(true));
                break;
            case CANCEL:
                setResponse(new Boolean(false));
                break;
            default:
                logger.warning("Invalid Actioncommand: invalid number.");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid Actioncommand: not a number.");
        }
    }

}
