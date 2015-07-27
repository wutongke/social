package io.rong.app.message;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;

/**
 * Created by Bob on 2015/4/17.
 */
@ProviderTag(messageContent = DeAgreedFriendRequestMessage.class, showPortrait = false, centerInHorizontal = false,showProgress = false,hide = true)
public  class DeAgreedFriendRequestMessageProvider extends IContainerItemProvider.MessageProvider<DeAgreedFriendRequestMessage> {
    @Override
    public void bindView(View v, int position, DeAgreedFriendRequestMessage content, Message message) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();

        if (content != null) {
            if (!TextUtils.isEmpty(content.getMessage()))
                viewHolder.contentTextView.setText(content.getMessage());
        }

    }

    @Override
    public Spannable getContentSummary(DeAgreedFriendRequestMessage data) {
        return null;
    }

    @Override
    public void onItemClick(View view, int position, DeAgreedFriendRequestMessage
            content, Message message) {
    }

    @Override
    public void onItemLongClick(View view, int i, DeAgreedFriendRequestMessage deAgreedFriendRequestMessage, Message message) {

    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.de_item_information_notification_message, group,false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.contentTextView = (TextView) view.findViewById(R.id.rc_msg);
        viewHolder.contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
        view.setTag(viewHolder);

        return view;
    }


    class ViewHolder {
        TextView contentTextView;
    }
}
