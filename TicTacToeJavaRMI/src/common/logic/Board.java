package common.logic;

import java.io.Serializable;

public class Board implements Serializable {

    //region Attributes
    private FieldState[][] fields;
    private int row;
    private int col;
    //endregion

    //region Constructor
    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        fields = new FieldState[row][col];
        cleanBoard();
    }
    //endregion

    //region Getters and Setters
    public FieldState[][] getFields() {
        return fields;
    }

    public void setFields(FieldState[][] board) {
        this.fields = board;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public FieldState getFieldState(int row, int col) {
        return fields[row][col];
    }

    public void setFieldState(int row, int col, FieldState fState) {
        fields[row][col] = fState;
    }
    //endregion

    /**
     * Empty all fields
     */
    private void cleanBoard() {
        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++)
                fields[r][c] = FieldState.FREE;
    }

    /**
     * Check that all cells are occupied
     *
     * @return If all the cells are occupied return true otherwise returns false
     */
    public boolean isLocked() {
        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++)
                if (fields[r][c] == FieldState.FREE)
                    return false;
        return true;
    }
}
