package com.gst.mybaseapp.customView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gst.mybaseapp.R;
import com.gst.mybaseapp.ui.MainActivity;


/**
 * Created by ThinkPad on 2015/12/25.
 */
public class CustomerAlertView extends Dialog {

    private Context mContext;

    public CustomerAlertView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomerAlertView(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    public static class Builder {
        private static final String TAG = "Builder";
        private Context context;
        private String title;
        private String message;
        private String hint;
        private int messageImg = -1;
        private String positiveButtonText = null;
        private String negativeButtonText = null;
        private View contentView;

        private SpannableString spStr;


        private View vBtnDiv;
        private TextView titleView;
        private TextView messageView;
        private TextView positiveButton;
        private TextView negativeButton;
        private ImageView messageImgView;
        //        private EditText mInputMsgEt;//输入框
//        private LinearLayout mInputMsgLl;//输入框布局
        private LinearLayout mMsgLl;//提示内容布局


        private boolean leftAlignment = false;
        private boolean cancalable = false;
        private boolean showInput = false;

        private OnClickListener positiveButtonClickListener,
                negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, boolean leftAlignment) {
            this.context = context;
            this.leftAlignment = leftAlignment;
        }

        public Builder(Context context, boolean leftAlignment, boolean showInput) {
            this.context = context;
            this.leftAlignment = leftAlignment;
            this.showInput = showInput;
        }

        /**
         * Set the Dialog message from String
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(SpannableString spStr) {
            this.spStr = spStr;
            return this;
        }

        public void setMessageAlignment(boolean left) {
            if (messageView != null) {
                messageView.setGravity(left ? Gravity.LEFT : Gravity.CENTER);
            }
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            return setTitle(context.getString(title));
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            if (title == null) {
                if (titleView != null) {
                    titleView.setVisibility(View.GONE);
                }
            } else {
                if (titleView != null) {
                    titleView.setVisibility(View.VISIBLE);
                    titleView.setText(title);
                }
            }
            return this;
        }

        /**
         * Set the Dialog title from drawable int
         *
         * @param messageImg
         * @return
         */
        public Builder setMessageImg(int messageImg) {
            this.messageImg = messageImg;
            return this;
        }


        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

//        /**
//         * 设置提示或输入框可见，ture为alert可见，false 为输入框可见
//         */
//        public void setAlertOrInput(boolean showInput) {
//            if (mInputMsgLl != null && mMsgLl != null) {
//
//                if (showInput) {
//                    mMsgLl.setVisibility(View.INVISIBLE);
//                    mInputMsgLl.setVisibility(View.VISIBLE);
//                } else {
//                    mMsgLl.setVisibility(View.VISIBLE);
//                    mInputMsgLl.setVisibility(View.INVISIBLE);
//                }
//            }
//        }
//
//        public Builder setEditTextHint(String hint) {
//            this.hint = hint;
//            if (hint != null) {
//                if (mInputMsgEt != null) {
//
//                    mInputMsgEt.setHint(hint);
//                }
//
//            }
//            return this;
//        }
//
//        /**
//         * 获得输入框中的内容
//         */
//        public String getInputContent() {
//            String content = "";
//            if (mInputMsgEt != null) {
//                content = mInputMsgEt.getText().toString().trim();
//            }
//
//            if (content != null && !content.equals("")) {
//                return content;
//            } else {
//                return "";
//            }
//        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }


        /**
         * Create the custom dialog
         */
        public CustomerAlertView create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomerAlertView dialog = new CustomerAlertView(context, R.style.CustomAlertDialog);
            View layout = inflater.inflate(R.layout.dialog_custom_alert, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            titleView = (TextView) layout.findViewById(R.id.custom_alert_title);
            messageView = (TextView) layout.findViewById(R.id.custom_alert_content);
            messageImgView = (ImageView) layout.findViewById(R.id.custom_alert_img);
            vBtnDiv = layout.findViewById(R.id.v_btn_div);
            setMessageAlignment(leftAlignment);
            mMsgLl = (LinearLayout) layout.findViewById(R.id.ll_custom_alert_content);//提示内容布局
//            mInputMsgEt = (EditText) layout.findViewById(R.id.et_input);//输入框
//            mInputMsgLl = (LinearLayout) layout.findViewById(R.id.ll_input_text);//输入框布局
//            setAlertOrInput(showInput);
            positiveButton = (TextView) layout.findViewById(R.id.custom_alert_positive);
            negativeButton = (TextView) layout.findViewById(R.id.custom_alert_negative);
            if (title == null) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(title);
            }
            // set the confirm button
            boolean needBtnDiv = true;
            if (positiveButtonText != null) {
                needBtnDiv = false;
                positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positiveButton
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                positiveButton.setVisibility(View.GONE);
            }

            if (negativeButtonText != null) {
                needBtnDiv = false;
                negativeButton.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    negativeButton
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                negativeButton.setVisibility(View.GONE);
            }

            if (needBtnDiv) {
                vBtnDiv.setVisibility(View.VISIBLE);
            } else {
                vBtnDiv.setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                messageView.setText(message);
            }

            if (spStr != null) {
                messageView.setText(spStr);
            }
            if (messageImg != -1) {
                messageImgView.setBackgroundResource(messageImg);
                messageImgView.setVisibility(View.VISIBLE);
            }
//            else if (contentView != null) {
//                // if no message set
//                // add the contentView to the dialog body
//                ((LinearLayout) layout.findViewById(R.id.custom_alert_content)).removeAllViews();
//                ((LinearLayout) layout.findViewById(R.id.custom_alert_content)).addView(
//                        contentView, new ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.WRAP_CONTENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//            }
            dialog.setContentView(layout);
            return dialog;
        }

        public CustomerAlertView show() {
            CustomerAlertView dialog = create();
            dialog.setCancelable(cancalable);
            if (!(context != null && context instanceof Activity && ((Activity) context).isFinishing())) {
                //当前Activity 关闭 dialog不在弹出
                dialog.show();
            }
            return dialog;
        }

        public boolean isCancelable() {
            return cancalable;
        }

        public void setCancelable(boolean Cancelable) {
            this.cancalable = Cancelable;
        }

        public boolean isLeftAlignment() {
            return leftAlignment;
        }

        public void setLeftAlignment(boolean leftAlignment) {
            this.leftAlignment = leftAlignment;
        }
    }

    /**
     * 测试dialog
     * @param context
     */
    public static void testDialog(Context context) {
        CustomerAlertView alertView = new CustomerAlertView.Builder(context, false, false)
                .setTitle("我是标题")
                .setMessage("我是内容")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
//                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
                .create();
        alertView.show();
    }
}
