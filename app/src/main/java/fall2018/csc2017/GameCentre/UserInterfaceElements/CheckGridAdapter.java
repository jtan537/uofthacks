package fall2018.csc2017.GameCentre.UserInterfaceElements;

/*
This Class is an overwrite of the built in Base Adapter class.
It is used for displaying each element of the scoreboard with GridView.
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.SuperClasses.GridViewAdapter;

/**
 * This is a sub-class of GridViewAdapter to be used to display elements of the leader board.
 */
public class CheckGridAdapter extends GridViewAdapter {

    /**
     * Constructor for initializing the adapter.
     * @param items :        Each element for each cell of the Grid View.
     * @param columnWidth :  The cell width for each column.
     * @param columnHeight : The cell height for each row.
     */
    CheckGridAdapter(ArrayList<View> items, int columnWidth, int columnHeight) {
        super(items, columnWidth, columnHeight);
    }

    /**
     * Returns the desired GridView layout at the specific position, overrides the super method
     *
     * @param position:    The given position.
     * @param convertView: Which type of view inputted.
     * @param parent:      The superclass of the view inputted.
     * @return TextView: The desired TextView at the position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View scoreItem;

        if (convertView == null) {
            scoreItem = (View) mItems.get(position);
        } else {
            scoreItem = (View) convertView;
        }

        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        scoreItem.setLayoutParams(params);
        return scoreItem;
    }
}
