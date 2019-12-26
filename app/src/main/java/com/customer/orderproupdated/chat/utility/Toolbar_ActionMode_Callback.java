package com.customer.orderproupdated.chat.utility;/*
package com.customer.orderproupdated.chat.utility;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;

import com.customer.orderproupdated.R;
import com.customer.orderproupdated.chat.bean.ChatBody;
import com.customer.orderproupdated.chat.fragments.ChatWindow;

import java.util.ArrayList;

*/
/**
 * Created by SONU on 22/03/16.
 *//*

public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    ChatWindow chatwindow;
    AdminBroadcast mAdminBroadcast;
    private ChatWindow.ChatListAdapter listView_adapter;

    private ArrayList<ChatBody> message_models;
    private int activityname;
    private Menu menu;

    public Toolbar_ActionMode_Callback(ChatWindow chatWindow, Context context, ChatWindow.ChatListAdapter listView_adapter, ArrayList<ChatBody> message_models, int activityname) {
        this.context = context;
        this.chatwindow = chatWindow;
        this.listView_adapter = listView_adapter;
        this.message_models = message_models;
        this.activityname = activityname;

    }



    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        this.menu = menu;

        mode.getMenuInflater().inflate(R.menu.action_menu, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {



        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_edit), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);

        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }

        MenuItem itemcopy = menu.findItem(R.id.action_copy);
        MenuItem itemedit = menu.findItem(R.id.action_edit);
        MenuItem itemedforward = menu.findItem(R.id.action_forward);

        itemedit.setVisible(false);
        itemedforward.setVisible(false);
        itemcopy.setVisible(false);

        if(PreferenceConstants.choicemod == 1)  // 1 not text
        {
            PreferenceConstants.choicemod = 0;
            itemcopy.setVisible(false);
            itemedforward.setVisible(true);


        }else if(PreferenceConstants.choicemod == 2) // 1 text
        {
            PreferenceConstants.choicemod = 0;
            itemcopy.setVisible(true);
            itemedit.setVisible(true);
            itemedforward.setVisible(true);

        }else if(PreferenceConstants.choicemod == 4) // 1 text
        {
            PreferenceConstants.choicemod = 0;
            itemcopy.setVisible(true);
            itemedit.setVisible(false);
            itemedforward.setVisible(true);

        }else if(PreferenceConstants.choicemod == 3) // mutiple
        {
            PreferenceConstants.choicemod = 0;
            itemcopy.setVisible(false);

        }else
        {
            PreferenceConstants.choicemod = 0;
            itemcopy.setVisible(true);

        }
        if(activityname  == 2)
        {
            itemedit.setVisible(false);
            itemedforward.setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                switch (activityname) {
                    case 0:
                        chatwindow.deleteRows();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;

                }

                break;
            case R.id.action_copy:

                //Get selected ids on basis of current fragment action mode
                SparseBooleanArray selected = null;

                switch (activityname) {
                    case 0:
                        selected = listView_adapter.getSelectedIds();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;

                }

                int selectedMessageSize = selected.size();

                //Loop to all selected items
                StringBuilder textMessage = new StringBuilder();

                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //get selected data in Model
                        ChatBody model = message_models.get(selected.keyAt(i));
                        String title = model.getMessage();
                        String subTitle = model.getMessageID();

                        String type = model.getMessageType();


                        textMessage.append(title);
                        textMessage.append("\n");
                       // com.albumgallery.Utility.Utility.setClipboard(textMessage.toString(), context);
                        //Print the data to show if its working properly or not
                      Log.e("Selected Items", "Title - " + title + "\n" + "id - " + subTitle +"\n" + "type - " + type);


                    }
                }
                mode.finish();//Finish action mode
                break;
            case R.id.action_edit:

                switch (activityname) {
                    case 0:
                        chatwindow.EditRows();
                        break;
                    case 1:
                        break;
                    case 2:
                }
                break;

            case R.id.action_forward:

                switch (activityname) {
                    case 0:
                        chatwindow.ForwardRows();
                        break;
                    case 1:
                        break;
                    case 2:
                }
                break;



        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        switch (activityname) {
            case 0:
                listView_adapter.removeSelection();  // remove selection
                chatwindow.setNullToActionMode();
                break;
            case 1:

                break;
            case 2:

                break;
        }


    }
}

*/
