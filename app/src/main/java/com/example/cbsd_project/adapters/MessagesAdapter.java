package com.example.cbsd_project.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cbsd_project.helpers.Constants;
import com.example.cbsd_project.R;
import com.example.cbsd_project.models.Message;

import java.util.List;

public class MessagesAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolderTextReceiver extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView textViewMessage;
        public TextView textViewSender;
        public ImageView imageViewUserAvatar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderTextReceiver(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            textViewMessage = (TextView) itemView.findViewById(R.id.item_message_receiver_textViewMessage);
            textViewSender = (TextView) itemView.findViewById(R.id.item_message_webview_receiver_textViewSender);
            imageViewUserAvatar = (ImageView) itemView.findViewById(R.id.item_message_webview_receiver_imageViewUserAvatar);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ON CREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(this.getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(this.getAdapterPosition(), 2, 2, "Delete");
            MenuItem Pin = menu.add(this.getAdapterPosition(), 3, 3, "Pin");
        }
    }

    public static class ViewHolderTextSender extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView textViewMessage;
        public TextView textViewSender;
        public ImageView imageViewUserAvatar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderTextSender(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            textViewMessage = (TextView) itemView.findViewById(R.id.item_message_sender_textViewMessage);
            textViewSender = (TextView) itemView.findViewById(R.id.item_message_sender_textViewSender);
            imageViewUserAvatar = (ImageView) itemView.findViewById(R.id.item_message_sender_imageViewUserAvatar);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ON CREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(this.getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(this.getAdapterPosition(), 2, 2, "Delete");
            MenuItem Pin = menu.add(this.getAdapterPosition(), 3, 3, "Pin");
        }
    }

    public static class ViewHolderWebReceiver extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public WebView webViewMessage;
        public TextView textViewSender;
        public ImageView imageViewUserAvatar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderWebReceiver(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            webViewMessage = (WebView) itemView.findViewById(R.id.item_message_webview_receiver_webViewMessage);
            textViewSender = (TextView) itemView.findViewById(R.id.item_message_webview_receiver_textViewSender);
            imageViewUserAvatar = (ImageView) itemView.findViewById(R.id.item_message_webview_receiver_imageViewUserAvatar);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ON CREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(this.getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(this.getAdapterPosition(), 2, 2, "Delete");
            MenuItem Pin = menu.add(this.getAdapterPosition(), 3, 3, "Pin");
        }
    }

    public static class ViewHolderWebSender extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public WebView webViewMessage;
        public TextView textViewSender;
        public ImageView imageViewUserAvatar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderWebSender(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            webViewMessage = (WebView) itemView.findViewById(R.id.item_message_webview_sender_webViewMessage);
            textViewSender = (TextView) itemView.findViewById(R.id.item_message_webview_sender_textViewSender);
            imageViewUserAvatar = (ImageView) itemView.findViewById(R.id.item_message_webview_sender_imageViewUserAvatar);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ON CREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(this.getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(this.getAdapterPosition(), 2, 2, "Delete");
            MenuItem Pin = menu.add(this.getAdapterPosition(), 3, 3, "Pin");
        }
    }

    public static class ViewHolderImageReceiver extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageViewPhoto;
        public TextView textViewSender;
        public ImageView imageViewUserAvatar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderImageReceiver(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageViewPhoto = (ImageView) itemView.findViewById(R.id.item_message_image_receiver_imageViewPhoto);
            textViewSender = (TextView) itemView.findViewById(R.id.item_message_image_receiver_textViewSender);
            imageViewUserAvatar = (ImageView) itemView.findViewById(R.id.item_message_image_receiver_imageViewUserAvatar);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ON CREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(this.getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(this.getAdapterPosition(), 2, 2, "Delete");
            MenuItem Pin = menu.add(this.getAdapterPosition(), 3, 3, "Pin");
        }
    }

    public static class ViewHolderImageSender extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageViewPhoto;
        public TextView textViewSender;
        public ImageView imageViewUserAvatar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderImageSender(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageViewPhoto = (ImageView) itemView.findViewById(R.id.item_message_image_sender_imageViewPhoto);
            textViewSender = (TextView) itemView.findViewById(R.id.item_message_image_sender_textViewSender);
            imageViewUserAvatar = (ImageView) itemView.findViewById(R.id.item_message_image_sender_imageViewUserAvatar);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ON CREATE MENU LISTENER
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(this.getAdapterPosition(), 1, 1, "Edit");
            MenuItem Delete = menu.add(this.getAdapterPosition(), 2, 2, "Delete");
            MenuItem Pin = menu.add(this.getAdapterPosition(), 3, 3, "Pin");
        }
    }


    // ... view holders defined above...

    // Store a member variable for the contacts
    public static List<Message> mMessages;

    private final int
            RECEIVER_WEB = 0,
            RECEIVER_IMAGE = 1,
            RECEIVER_TEXT = 2,
            RECEIVER_LOCATION = 3,
            SENDER_WEB = 4,
            SENDER_IMAGE = 5,
            SENDER_TEXT = 6,
            SENDER_LOCATION = 7;

    Context context;

    // Pass in the contact array into the constructor
    public MessagesAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getMessageType() != null) {

            switch (mMessages.get(position).getMessageType()) {
                case Constants.MessageTypeText:
                    if (mMessages.get(position).getMessageViewType() != null) {
                        return ((mMessages.get(position).getMessageViewType()).
                                equals(Constants.MessageViewTypeReceiver))?
                                RECEIVER_TEXT : SENDER_TEXT;
                    }
                    break;
                case Constants.MessageTypeImage:
                    if (mMessages.get(position).getMessageViewType() != null) {
                        return ((mMessages.get(position).getMessageViewType()).
                                equals(Constants.MessageViewTypeReceiver))?
                                RECEIVER_IMAGE : SENDER_IMAGE;
                    }
                    break;
                case Constants.MessageTypeGif:
                    if (mMessages.get(position).getMessageViewType() != null) {
                        return ((mMessages.get(position).getMessageViewType()).
                                equals(Constants.MessageViewTypeReceiver))?
                                RECEIVER_WEB : SENDER_WEB;
                    }
                    break;
                case Constants.MessageTypeLocation:
                    if (mMessages.get(position).getMessageViewType() != null) {
                        return ((mMessages.get(position).getMessageViewType()).
                                equals(Constants.MessageViewTypeReceiver))?
                                RECEIVER_LOCATION : SENDER_LOCATION;
                    }
                    break;
            }

        }

        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == RECEIVER_TEXT) {
            View v1 = inflater.inflate(R.layout.item_message_text_receiver, parent, false);
            viewHolder = new ViewHolderTextReceiver(v1);
            return viewHolder;
        } else
        if (viewType == SENDER_TEXT) {
            View v2 = inflater.inflate(R.layout.item_message_text_sender, parent, false);
            viewHolder = new ViewHolderTextSender(v2);
            return viewHolder;
        } else
        if (viewType == RECEIVER_WEB) {
            View v3 = inflater.inflate(R.layout.item_message_webview_receiver, parent, false);
            viewHolder = new ViewHolderWebReceiver(v3);
            return viewHolder;
        } else
        if (viewType == SENDER_WEB) {
            View v4 = inflater.inflate(R.layout.item_message_webview_sender, parent, false);
            viewHolder = new ViewHolderWebSender(v4);
            return viewHolder;
        } else
        if (viewType == RECEIVER_IMAGE) {
            View v1 = inflater.inflate(R.layout.item_message_image_receiver, parent, false);
            viewHolder = new ViewHolderImageReceiver(v1);
            return viewHolder;
        } else
        if (viewType == SENDER_IMAGE) {
            View v2 = inflater.inflate(R.layout.item_message_image_sender, parent, false);
            viewHolder = new ViewHolderImageSender(v2);
            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Message message = mMessages.get(position);

        if (holder.getItemViewType() == RECEIVER_TEXT) {
            ViewHolderTextReceiver viewHolderTextReceiver = (ViewHolderTextReceiver) holder;
            configureViewHolderTextReceiver(viewHolderTextReceiver, message);
        } else
        if (holder.getItemViewType() == SENDER_TEXT) {
            ViewHolderTextSender viewHolderTextSender = (ViewHolderTextSender) holder;
            configureViewTextHolderSender(viewHolderTextSender, message);
        } else
        if (holder.getItemViewType() == RECEIVER_WEB) {
            ViewHolderWebReceiver viewHolderWebReceiver = (ViewHolderWebReceiver) holder;
            configureViewHolderWebReceiver(viewHolderWebReceiver, message);
        } else
        if (holder.getItemViewType() == SENDER_WEB) {
            ViewHolderWebSender viewHolderWebSender = (ViewHolderWebSender) holder;
            configureViewHolderWebSender(viewHolderWebSender, message);
        } else
        if (holder.getItemViewType() == RECEIVER_IMAGE) {
            ViewHolderImageReceiver viewHolderImageReceiver = (ViewHolderImageReceiver) holder;
            configureViewHolderImageReceiver(viewHolderImageReceiver, message);
        } else
        if (holder.getItemViewType() == SENDER_IMAGE) {
            ViewHolderImageSender viewHolderImageSender = (ViewHolderImageSender) holder;
            configureViewHolderImageSender(viewHolderImageSender, message);
        }
    }

    private void configureViewHolderTextReceiver(ViewHolderTextReceiver viewHolderTextReceiver, Message message) {
        if (message != null) {
            TextView textViewMessage = viewHolderTextReceiver.textViewMessage;
            textViewMessage.setText(message.getContent());
            TextView textViewSender = viewHolderTextReceiver.textViewSender;
            textViewSender.setText(message.getSender());
            ImageView imageViewUserAvatar = viewHolderTextReceiver.imageViewUserAvatar;
        }
    }

    private void configureViewTextHolderSender(ViewHolderTextSender viewHolderTextSender, Message message) {
        if (message != null) {
            TextView textViewMessage = viewHolderTextSender.textViewMessage;
            textViewMessage.setText(message.getContent());
            TextView textViewSender = viewHolderTextSender.textViewSender;
            textViewSender.setText(message.getSender());
            ImageView imageViewUserAvatar = viewHolderTextSender.imageViewUserAvatar;
        }
    }

    private void configureViewHolderWebReceiver(ViewHolderWebReceiver viewHolderWebReceiver, Message message) {
        if (message != null) {
            WebView webViewMessage = viewHolderWebReceiver.webViewMessage;
            webViewMessage.loadUrl(message.getContent());
            webViewMessage.setBackgroundColor(Color.TRANSPARENT);
            TextView textViewSender = viewHolderWebReceiver.textViewSender;
            textViewSender.setText(message.getSender());
            ImageView imageViewUserAvatar = viewHolderWebReceiver.imageViewUserAvatar;
        }
    }

    private void configureViewHolderWebSender(ViewHolderWebSender viewHolderWebSender, Message message) {
        if (message != null) {
            WebView webViewMessage = viewHolderWebSender.webViewMessage;
            webViewMessage.loadUrl(message.getContent());
            webViewMessage.getSettings().setUseWideViewPort(true);
            webViewMessage.getSettings().setLoadWithOverviewMode(true);
            webViewMessage.setBackgroundColor(Color.TRANSPARENT);
            TextView textViewSender = viewHolderWebSender.textViewSender;
            textViewSender.setText(message.getSender());
            ImageView imageViewUserAvatar = viewHolderWebSender.imageViewUserAvatar;
        }
    }

    private void configureViewHolderImageReceiver(ViewHolderImageReceiver viewHolderImageReceiver, Message message) {
        if (message != null) {
            ImageView imageViewPhoto = viewHolderImageReceiver.imageViewPhoto;
            Glide.with(context)
                    .load(message.getContent())
                    .into(imageViewPhoto);

            TextView textViewSender = viewHolderImageReceiver.textViewSender;
            textViewSender.setText(message.getSender());
            ImageView imageViewUserAvatar = viewHolderImageReceiver.imageViewUserAvatar;
        }
    }

    private void configureViewHolderImageSender(ViewHolderImageSender viewHolderImageSender, Message message) {
        if (message != null) {
            ImageView imageViewPhoto = viewHolderImageSender.imageViewPhoto;
            Glide.with(context)
                    .load(message.getContent())
                    .into(imageViewPhoto);

            TextView textViewSender = viewHolderImageSender.textViewSender;
            textViewSender.setText(message.getSender());
            ImageView imageViewUserAvatar = viewHolderImageSender.imageViewUserAvatar;
        }
    }

}