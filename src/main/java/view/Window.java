package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Cell;
import model.CellStatus;
import model.GameLevel;
import model.MineSweeperModel;
import model.RightClickStatus;

public class Window extends JFrame {

    public MineSweeperModel mineSweeperModel;

    public int rowCount = 8;
    public int colCount = 10;

    public int numberofBombs = 10;

    public GameLevel boardGameLevel;

    public boolean isGameStarted = false;


    public TagJButton[][] buttons;


    public JPanel flagCountPanel;
    public JPanel boardPanel;
    public JLabel flagLabel;
    public JLabel flagCountLabel;


    public Window() {

        initializeComponents();

        mineSweeperModel = new MineSweeperModel();


        mineSweeperModel.SetupNewBoard(boardGameLevel);

        //Test_ViewBoard();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setSize(500, 325);

        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void initializeComponents() {
        GameLevel gameLevel = GameLevel.EASY;

        boardGameLevel = gameLevel;

        switch (gameLevel) {
            case EASY:
                numberofBombs = 10;
                rowCount = 8;
                colCount = 10;
                break;

            case MEDIUM:
                numberofBombs = 40;
                rowCount = 14;
                colCount = 18;
                break;

            case HARD:
                numberofBombs = 99;
                rowCount = 20;
                colCount = 24;
                break;
        }


        buttons = new TagJButton[rowCount][colCount];

        MouseListener mouseListener = new MouseClicked();


        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(rowCount, colCount));

        for (int i = 0; i < rowCount; i++)
            for (int j = 0; j < colCount; j++) {
                buttons[i][j] = new TagJButton(new Point(j, i));
                (buttons[i][j]).addMouseListener(mouseListener);

                buttons[i][j].setBackground(Color.LIGHT_GRAY);

                boardPanel.add(buttons[i][j]);

            }

        setLayout(new BorderLayout());

        flagCountPanel = new JPanel();
        flagCountLabel = new JLabel();
        flagCountLabel.setText(Integer.toString(numberofBombs));
        flagLabel = new JLabel(FLAG);
        flagCountPanel.add(flagLabel);
        flagCountPanel.add(flagCountLabel);

        add(flagCountPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

    }


    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public int getNumberofBombs() {
        return numberofBombs;
    }

    public void setNumberofBombs(int numberofBombs) {
        this.numberofBombs = numberofBombs;
    }

    public GameLevel getBoardGameLevel() {
        return boardGameLevel;
    }

    public void setBoardGameLevel(GameLevel boardGameLevel) {
        this.boardGameLevel = boardGameLevel;
    }

    public Boolean getIsGameStarted() {
        return isGameStarted;
    }

    public void setIsGameStarted(Boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }
//}

    @SuppressWarnings("serial")
    class TagJButton<T> extends JButton {
        private T tag;

        public TagJButton(T tag) {
            super();
            this.tag = tag;
        }

        public T getTag() {
            return tag;
        }
    }

    private void Test_ViewBoard() {

        Cell cell;
        CellStatus cStatus;

        for (int i = 0; i < rowCount; i++)
            for (int j = 0; j < colCount; j++) {
                Cell board[][] = mineSweeperModel.getBoard();
                cell = board[i][j];
                cStatus = cell.getCellStatus();

                TagJButton b = buttons[i][j];

                switch (cStatus) {
                    case BLANK:
                        b.setText("");

                        break;
                    case BOMB:
                        b.setText(BOMB);

                        break;

                    case NUMBER:
                        b.setText(Integer.toString(cell.getNumber()));

                        break;
                }

            }
    }

    private static final String FLAG = "\uD83D\uDEA9";
    private static final String BOMB = "\uD83D\uDCA3";
    private static final String QMARK = "?";

    class MouseClicked extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                TagJButton button = (TagJButton) e.getSource();
                Point p = (Point) button.getTag();

                System.err.println(p);
                System.err.println("Left-click on button");

                List<Cell> cells = mineSweeperModel.LeftClick(p);

                for (Cell cell : cells) {
                    TagJButton cellButton = buttons[cell.getLocation().y][cell.getLocation().x];

                    if (cell.getRightClickStatus() == RightClickStatus.BLANK) {
                        if (cell.getCellStatus() == CellStatus.BOMB) {

                            cellButton.setText(BOMB);
                            System.err.println("GAME OVER. Lost...");

                            for (Cell bombCell : mineSweeperModel.getListOfBombs()) {
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].setBackground(Color.RED);
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].setText(BOMB);
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].setEnabled(false);
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].removeMouseListener(this);
                            }

                            for (int i = 0; i < rowCount; i++)
                                for (int j = 0; j < colCount; j++) {
                                    buttons[i][j].setEnabled(false);
                                    buttons[i][j].removeMouseListener(this);
                                }


                        } else if (cell.getCellStatus() == CellStatus.NUMBER) {

                            switch (cell.getNumber()) {

                                case 1:
                                    cellButton.setForeground(Color.BLUE);
                                    break;
                                case 2:
                                    cellButton.setForeground(Color.GREEN);
                                    break;
                                case 3:
                                    cellButton.setForeground(Color.RED);
                                    break;
                                case 4:
                                    cellButton.setForeground(Color.MAGENTA.darker());//Should be Purple
                                    break;
                            }
                            cellButton.setText(Integer.toString(cell.getNumber()));
                        }
                        cellButton.setBackground(Color.WHITE);

                        cellButton.setEnabled(false);
                        cellButton.removeMouseListener(this);
                        if (mineSweeperModel.IsGameWon()) {

                            System.err.println("YOU WON!");

                            for (Cell bombCell : mineSweeperModel.getListOfBombs()) {
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].setBackground(Color.YELLOW);
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].setText(BOMB);

                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].setEnabled(false);
                                buttons[bombCell.getLocation().y][bombCell.getLocation().x].removeMouseListener(this);
                            }
                        }
                    }

                }


            } else if (e.getButton() == MouseEvent.BUTTON3) {

                TagJButton button = (TagJButton) e.getSource();
                Point p = (Point) button.getTag();

                System.err.println(p);
                System.err.println("Right-click on button");
                RightClickStatus rightClickStatus = mineSweeperModel.RightClick(p);

                switch (rightClickStatus) {
                    case BLANK:
                        button.setText("");
                        button.setBackground(Color.LIGHT_GRAY);
                        button.setForeground(Color.BLACK);

                        flagCountLabel.setText(Integer.toString(mineSweeperModel.getNumRemainingBombsFlagged()));

                        break;
                    case FLAG:
                        button.setText(FLAG);
                        button.setBackground(Color.ORANGE.brighter());
                        button.setForeground(Color.RED);

                        flagCountLabel.setText(Integer.toString(mineSweeperModel.getNumRemainingBombsFlagged()));

                        break;
                    case QMARK:
                        button.setText(QMARK);
                        button.setBackground(Color.GREEN.brighter());
                        button.setForeground(Color.GREEN.darker());
                        break;
                }

            }
        }
    }
}
