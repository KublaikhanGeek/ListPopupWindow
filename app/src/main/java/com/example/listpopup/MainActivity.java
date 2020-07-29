package com.example.listpopup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Regular Android menu item that contains a title and an icon if icon is specified.
     */
    private static final int STANDARD_MENU_ITEM = 0;

    /**
     * Menu item that has two buttons, the first one is a title and the second one is an icon.
     * It is different from the regular menu item because it contains two separate buttons.
     */
    private static final int TITLE_BUTTON_MENU_ITEM = 1;

    /**
     * Menu item that has three buttons. Every one of these buttons is displayed as an icon.
     */
    private static final int THREE_BUTTON_MENU_ITEM = 2;

    /**
     * Menu item that has four buttons. Every one of these buttons is displayed as an icon.
     */
    private static final int FOUR_BUTTON_MENU_ITEM = 3;

    /**
     * Menu item that has five buttons. Every one of these buttons is displayed as an icon.
     */
    private static final int FIVE_BUTTON_MENU_ITEM = 4;

    /**
     * The number of view types specified above.  If you add a view type you MUST increment this.
     */
    private static final int VIEW_TYPE_COUNT = 5;

    /** IDs of all of the buttons in icon_row_menu_item.xml. */
    private static final int[] BUTTON_IDS = {
            R.id.button_one,
            R.id.button_two,
            R.id.button_three,
            R.id.button_four,
            R.id.button_five
    };

    private ListPopupWindow mListPop;
    private EditText mEditText;
//    private List<String> lists = new ArrayList<String>();
    private Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        lists.add("one");
        lists.add("two");
        lists.add("three");
        mEditText = (EditText) findViewById(R.id.editText1);
        mListPop = new ListPopupWindow(this);
        mListPop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists));
//        mListPop.setWidth(LayoutParams.WRAP_CONTENT);
 //       mListPop.setHeight(LayoutParams.WRAP_CONTENT);
        mListPop.setAnchorView(mEditText);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);//设置是否是模式
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mEditText.setText(lists.get(position));
                mListPop.dismiss();
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListPop.show();
            }
        });
        */
        mEditText = (EditText) findViewById(R.id.editText1);
        initPopView();
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListPop.show();
            }
        });
    }

    private void initPopView()
    {
        if (mMenu == null)
        {
            PopupMenu tempMenu = new PopupMenu(this, mEditText);
            tempMenu.inflate(R.menu.main_menu);
            mMenu = tempMenu.getMenu();
        }

        int numItems = mMenu.size();
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        for (int i = 0; i < numItems; ++i) {
            MenuItem item = mMenu.getItem(i);
            if (item.isVisible()) {
                menuItems.add(item);
            }
        }

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        mListPop = new ListPopupWindow(this, null, android.R.attr.popupMenuStyle);
        mListPop.setModal(true);//设置是否是模式
        mListPop.setAnchorView(mEditText);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        Rect bgPadding = new Rect();
        mListPop.getBackground().getPadding(bgPadding);

        int popupWidth = this.getResources().getDimensionPixelSize(R.dimen.menu_width)
                + bgPadding.left + bgPadding.right;

        mListPop.setWidth(popupWidth);
        BaseAdapter listAdapter = new TypedListAdapter(menuItems, inflater);
        mListPop.setAdapter(listAdapter);
    }

    private class TypedListAdapter extends BaseAdapter
    {
        private List<MenuItem> mMenuItems;
        private LayoutInflater mInflater;
        public TypedListAdapter(List<MenuItem> datas, LayoutInflater inflater)
        {
            mInflater = inflater;
            mMenuItems = datas;
        }
        @Override
        public int getViewTypeCount()
        {
            return VIEW_TYPE_COUNT;
        }
        @Override
        public int getItemViewType(int position)
        {
            MenuItem item = getItem(position);
            int viewCount = item.hasSubMenu() ? item.getSubMenu().size() : 1;

            if (viewCount == 5) {
                return FIVE_BUTTON_MENU_ITEM;
            } else if (viewCount == 4) {
                return FOUR_BUTTON_MENU_ITEM;
            } else if (viewCount == 3) {
                return THREE_BUTTON_MENU_ITEM;
            } else if (viewCount == 2) {
                return TITLE_BUTTON_MENU_ITEM;
            }
            return STANDARD_MENU_ITEM;
        }
        @Override
        public int getCount()
        {
            return mMenuItems.size();
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getItemId();
        }

        @Override
        public MenuItem getItem(int position) {
            if (position == ListView.INVALID_POSITION) return null;
            assert position >= 0;
            assert position < mMenuItems.size();
            return mMenuItems.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            int type = getItemViewType(position);
            final MenuItem item = getItem(position);
            switch (getItemViewType(position)) {
                case STANDARD_MENU_ITEM: {
                    StandardMenuItemViewHolder holder = null;
                    if (convertView == null
                            || !(convertView.getTag() instanceof StandardMenuItemViewHolder)) {
                        holder = new StandardMenuItemViewHolder();
                        convertView = mInflater.inflate(R.layout.menu_item, parent, false);
                        holder.text = (TextView) convertView.findViewById(R.id.menu_item_text);
                        holder.image = (ImageView) convertView.findViewById(R.id.menu_item_icon);
                        convertView.setTag(holder);
                    } else {
                        holder = (StandardMenuItemViewHolder) convertView.getTag();
                    }

                    setupStandardMenuItemViewHolder(holder, convertView, item);
                    break;
                }
                case FIVE_BUTTON_MENU_ITEM: {
                    convertView = createMenuItemRow(convertView, parent, item, 5);
                    break;
                }
                default:
                    assert false : "Unexpected MenuItem type";
            }
            return convertView;
        }

        private void setupStandardMenuItemViewHolder(StandardMenuItemViewHolder holder,
                                                     View convertView, final MenuItem item) {
            // Set up the icon.
            Drawable icon = item.getIcon();
            holder.image.setImageDrawable(icon);
            holder.image.setVisibility(icon == null ? View.GONE : View.VISIBLE);
            holder.text.setText(item.getTitle());
            holder.text.setContentDescription(item.getTitleCondensed());

            boolean isEnabled = item.isEnabled();
            // Set the text color (using a color state list).
            holder.text.setEnabled(isEnabled);
            // This will ensure that the item is not highlighted when selected.
            convertView.setEnabled(isEnabled);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 //                   mAppMenu.onItemClick(item);
                }
            });
        }

        private View createMenuItemRow(
                View convertView, ViewGroup parent, MenuItem item, int numItems) {
            RowItemViewHolder holder = null;
            if (convertView == null
                    || !(convertView.getTag() instanceof RowItemViewHolder)
                    || ((RowItemViewHolder) convertView.getTag()).buttons.length != numItems) {
                holder = new RowItemViewHolder(numItems);
                convertView = mInflater.inflate(R.layout.icon_row_menu_item, parent, false);

                // Save references to all the buttons.
                for (int i = 0; i < numItems; i++) {
                    holder.buttons[i] =
                            (ImageView) convertView.findViewById(BUTTON_IDS[i]);
                }

                // Remove unused menu items.
                for (int j = numItems; j < 5; j++) {
                    ((ViewGroup) convertView).removeView(convertView.findViewById(BUTTON_IDS[j]));
                }

                convertView.setTag(holder);
            } else {
                holder = (RowItemViewHolder) convertView.getTag();
            }

            for (int i = 0; i < numItems; i++) {
                setupImageButton(holder.buttons[i], item.getSubMenu().getItem(i));
            }
            convertView.setFocusable(false);
            convertView.setEnabled(false);
            return convertView;
        }

        private void setupImageButton(ImageView button, final MenuItem item) {
            // Store and recover the level of image as button.setimageDrawable
            // resets drawable to default level.
            int currentLevel = item.getIcon().getLevel();
            button.setImageDrawable(item.getIcon());
            item.getIcon().setLevel(currentLevel);
            button.setEnabled(item.isEnabled());
            button.setFocusable(item.isEnabled());
            button.setContentDescription(item.getTitleCondensed());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mAppMenu.onItemClick(item);
                }
            });

            // Menu items may be hidden by command line flags before they get to this point.
            button.setVisibility(item.isVisible() ? View.VISIBLE : View.GONE);
        }

    }


    static class StandardMenuItemViewHolder
    {
        public TextView text;
        public ImageView image;
    }

    private static class RowItemViewHolder
    {
        public ImageView[] buttons;

        RowItemViewHolder(int numButtons) {
            buttons = new ImageView[numButtons];
        }
    }

}
