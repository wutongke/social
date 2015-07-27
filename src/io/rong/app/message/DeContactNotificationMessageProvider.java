package io.rong.app.message;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.ContactNotificationMessage;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
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
@ProviderTag(messageContent = ContactNotificationMessage.class, showPortrait = false, centerInHorizontal = true,showProgress = false)
public  class DeContactNotificationMessageProvider extends IContainerItemProvider.MessageProvider<ContactNotificationMessage> {
    @Override
    public void bindView(View v, int position, ContactNotificationMessage content, Message message) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();

        if (content != null) {
            if (!TextUtils.isEmpty(content.getMessage()))
                viewHolder.contentTextView.setText(content.getMessage());
        }

    }

    @Override
    public Spannable getContentSummary(ContactNotificationMessage data) {
        if (data != null && !TextUtils.isEmpty(data.getMessage()))
            return new SpannableString(data.getMessage());
        return null;
    }

    @Override
    public void onItemClick(View view, int position, ContactNotificationMessage
            content, Message message) {
    }

    @Override
    public void onItemLongClick(View view, int i, ContactNotificationMessage contactNotificationMessage, Message message) {

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
