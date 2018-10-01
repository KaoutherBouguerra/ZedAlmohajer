package com.art4muslim.zedalmouhajer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.models.Comment;
import com.art4muslim.zedalmouhajer.models.InfoApp;
import com.art4muslim.zedalmouhajer.models.News;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.art4muslim.zedalmouhajer.view.LoadingFeedItemView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserIMAGE;
import static com.art4muslim.zedalmouhajer.utils.Utils.printDifference;

/**
 * Created by kaoutherbouguerra on 05.09.18.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    private final List<FeedItem> feedItems = new ArrayList<>();

    public static Context context;
    BaseApplication app;
    private ArrayList<News> actualiteArrayList = new ArrayList<>();
  //  private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;

    public FeedAdapter(Context context, ArrayList<News> actualiteArrayList) {
        this.context = context;
        this.actualiteArrayList = actualiteArrayList;
        app = (BaseApplication) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEFAULT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
            CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
            setupClickableViews(view, cellFeedViewHolder);
            return cellFeedViewHolder;
        } else if (viewType == VIEW_TYPE_LOADER) {
            LoadingFeedItemView view = new LoadingFeedItemView(context);
            view.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            return new LoadingCellFeedViewHolder(view);
        }

        return null;
    }

    private void setupClickableViews(final View view, final CellFeedViewHolder cellFeedViewHolder) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((CellFeedViewHolder) viewHolder).bindView(feedItems.get(position));

        if (getItemViewType(position) == VIEW_TYPE_LOADER) {
            bindLoadingFeedItem((LoadingCellFeedViewHolder) viewHolder);
        }
    }

    private void bindLoadingFeedItem(final LoadingCellFeedViewHolder holder) {
        holder.loadingFeedItemView.setOnLoadingFinishedListener(new LoadingFeedItemView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
                showLoadingView = false;
                notifyItemChanged(0);
            }
        });
        holder.loadingFeedItemView.startLoading();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public void updateItems(boolean animated, ArrayList<News> actualiteArrayList) {
        feedItems.clear();

        for (News news: actualiteArrayList){
            if (app.getAssociation() != null)
            feedItems.add(new FeedItem( news.getId(), news.getName(), news.getDetails(),
                    news.getDate(), news.getImage(),app.getAssociation().getImage(),news.getYoutubelink()));
            else
                feedItems.add(new FeedItem( news.getId(), news.getName(), news.getDetails(),
                        news.getDate(), news.getImage(),BaseApplication.session.getUserDetails().get(Key_UserIMAGE),news.getYoutubelink()));
        }

        if (animated) {
            notifyItemRangeInserted(0, feedItems.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void showLoadingView() {
        showLoadingView = true;
        notifyItemChanged(0);
    }



    public static class CellFeedViewHolder extends RecyclerView.ViewHolder   {
        @BindView(R.id.ivFeedCenter)
        ImageView ivFeedCenter;

        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView youtubeView;

        @BindView(R.id.relativeLayout_over_youtube_thumbnail)
        RelativeLayout relativeLayoutOverYoutubeThumbnail;

        @BindView(R.id.ivFeedBottom)
        TextView ivFeedBottom;

        @BindView(R.id.ivUserProfile)
        ImageView ivUserProfile;

        @BindView(R.id.btnYoutube_player)
        ImageView playBtn;

        @BindView(R.id.ivTitle)
        TextView ivTitle;

        @BindView(R.id.ivDate)
        TextView ivDate;

        @BindView(R.id.vImageRoot)
        FrameLayout vImageRoot;

        @BindView(R.id.cardView)
        CardView vVideoRoot;

        @BindView(R.id.ivUserCommentProfile)
        ImageView ivUserCommentProfile;

        @BindView(R.id.edt_text)
        EditText edtText;

        @BindView(R.id.layout_comments)
        LinearLayout layoutComments;

        FeedItem feedItem;

        private static final int RECOVERY_REQUEST = 1;
        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(final FeedItem feedItem) {
            this.feedItem = feedItem;

            if (feedItem.url_image != null && !feedItem.url_image.isEmpty())
            Picasso.with(context)
                    .load(feedItem.url_image)
                    .into(ivUserProfile);

            if (feedItem.url_news_image != null && !feedItem.url_news_image.isEmpty()
                    && (feedItem.url_news_image.endsWith(".jpg")
                    || feedItem.url_news_image.endsWith(".png")
                    || feedItem.url_news_image.endsWith(".jpeg")
                    || feedItem.url_news_image.endsWith(".svg"))) {
                vImageRoot.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(feedItem.url_news_image)
                        .into(ivFeedCenter);
            } else vImageRoot.setVisibility(View.GONE);

            getAllComments();

            if (feedItem.url_video != null && !feedItem.url_video.isEmpty() && feedItem.url_video.contains("watch?v=")) {
                vVideoRoot.setVisibility(View.VISIBLE);
                vImageRoot.setVisibility(View.GONE);


                final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Log.e("video success"," error onThumbnailLoaded " +errorReason.toString());
                    }

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        relativeLayoutOverYoutubeThumbnail.setVisibility(View.VISIBLE);
                        Log.e("video success"," success onThumbnailLoaded " );
                     }
                };


                youtubeView.initialize(Constants.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        Log.e("video success"," success reading video " );
                        if (feedItem.url_video.contains("watch?v=")) {
                            String[] str = feedItem.url_video.split("v=");
                            String id = str[1];
                            youTubeThumbnailLoader.setVideo(id);

                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                        }else{
                            vVideoRoot.setVisibility(View.GONE);
                        }



                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        //write something for failure
                         Log.e("video failure"," error "+youTubeInitializationResult.toString());
                    }
                });

                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (feedItem.url_video.contains("watch?v=")) {
                            String[] str = feedItem.url_video.split("v=");
                            String id = str[1];
                            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, Constants.YOUTUBE_API_KEY, id);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            else vVideoRoot.setVisibility(View.GONE);

            Log.e("Feed adapter"," url_image ass profile = "+feedItem.url_image);
            Log.e("Feed adapter"," url_image news = "+feedItem.url_news_image);
            Log.e("Feed adapter"," description = "+feedItem.description);
            Log.e("Feed adapter"," titre = "+feedItem.titre);
            Log.e("Feed adapter"," date = "+feedItem.date);
            Log.e("Feed adapter"," url_video = "+feedItem.url_video);
            ivFeedBottom.setText(feedItem.description);
            ivTitle.setText(feedItem.titre);


            Calendar c = Calendar.getInstance();
            String format = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("en","EN","EN"));
            String formattedDate = sdf.format(c.getTime());
            Date dateNow = new Date();
            Date dateOrder = new Date();

            try {

                dateNow = sdf.parse(formattedDate);
                dateOrder = sdf.parse(feedItem.date);
                ivDate.setText(printDifference(dateOrder, dateNow));

            } catch (ParseException e) {
                Log.e("######## ","###### ParseException ###### "+e.getMessage());
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        Log.i("feed news","Enter pressed");
                        addComment(feedItem.titre ,edtText.getText().toString(), feedItem.id_actualite);
                    }
                    return false;
                }
            });

        }

        public FeedItem getFeedItem() {
            return feedItem;
        }

        private void addViews( Comment comment) {
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,

                    3));
            view.setBackgroundResource(android.R.color.darker_gray);
           View v = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
           layoutComments.addView(v);
           layoutComments.addView(view);
           layoutComments.requestLayout();
           TextView date = (TextView) v.findViewById(R.id.ivDate);
           TextView title = (TextView) v.findViewById(R.id.ivTitle);
           TextView com = (TextView) v.findViewById(R.id.ivFeedBottom);
           ImageView ivUserProfile = (ImageView) v.findViewById(R.id.ivUserProfile);






            title.setText(comment.getName());
            com.setText(comment.getComment());


            Calendar c = Calendar.getInstance();
            String format = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("en","EN","EN"));
            String formattedDate = sdf.format(c.getTime());
            Date dateNow = new Date();
            Date dateOrder = new Date();

            try {

                dateNow = sdf.parse(formattedDate);
                dateOrder = sdf.parse(feedItem.date);
                date.setText(printDifference(dateOrder, dateNow));

            } catch (ParseException e) {
                Log.e("######## ","###### ParseException ###### "+e.getMessage());
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void addComment(final String name, final String comment, final String idNews) {

            String url = Constants.ADD_COMMENT;
            //+"phone="+phone+"&password="+password;

            Log.e("feed news", "addComment url "+url);


            StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.v("addComment","response == " +response);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        boolean _status = jsonObject.getBoolean("status");
                        String msg = jsonObject.getString("message");
                        if (_status) {
                            AlertDialogManager.showAlertDialog(context,context.getResources().getString(R.string.app_name),msg,true,0);


                        } else {


                            AlertDialogManager.showAlertDialog(context,context.getResources().getString(R.string.app_name),msg,false,0);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    if (error instanceof AuthFailureError) {

                        AlertDialogManager.showAlertDialog(context,context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.authontiation),false,3);

                    } else if (error instanceof ServerError) {
                        AlertDialogManager.showAlertDialog(context,context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.servererror),false,3);
                    } else if (error instanceof NetworkError) {
                        AlertDialogManager.showAlertDialog(context,context.getResources().getString(R.string.networkerror),context.getResources().getString(R.string.networkerror),false,3);

                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        AlertDialogManager.showAlertDialog(context,context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.timeouterror),false,3);
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("comment", comment);

                    params.put("id_news", ""+ idNews);

                    return params;
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };


            // Adding request to request queue
            BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


        }

        private void getAllComments(){

            String url = Constants.GET_ALL_COMMENTS+ feedItem.id_actualite;
            Log.e("get comments", "getAllComments url "+url);

            StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Gson gson = new Gson();
                    Log.e("get comments", "getAllComments response === "+response.toString());

                    try {
                        JSONArray resJsonArr = new JSONArray(response);

                        for (int i = 0; i< resJsonArr.length(); i++) {
                            JSONObject resJsonObj = resJsonArr.getJSONObject(i);
                            Comment comment = gson.fromJson(String.valueOf(resJsonObj), Comment.class);

                            addViews(comment);

                            //   app.setInfoApp(infoApp);
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("/////// VOLLEY  ///// ", error.toString());
                    AlertDialogManager.showAlertDialog(context, context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.usernameandpassword),false,3);

                    if (error instanceof AuthFailureError) {
                        AlertDialogManager.showAlertDialog(context, context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.authontiation),false,3);

                    } else if (error instanceof ServerError) {
                        AlertDialogManager.showAlertDialog(context, context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.servererror),false,3);
                    } else if (error instanceof NetworkError) {
                        AlertDialogManager.showAlertDialog(context, context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.networkerror),false,3);

                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        AlertDialogManager.showAlertDialog(context, context.getResources().getString(R.string.app_name),context.getResources().getString(R.string.timeouterror),false,3);
                    }

                }
            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {

                        String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };

            // Adding request to request queue
            BaseApplication.getInstance().addToRequestQueue(hisRequest);
        }
    }

    public static class LoadingCellFeedViewHolder extends CellFeedViewHolder {

        LoadingFeedItemView loadingFeedItemView;

        public LoadingCellFeedViewHolder(LoadingFeedItemView view) {
            super(view);
            this.loadingFeedItemView = view;
        }

        @Override
        public void bindView(FeedItem feedItem) {
            super.bindView(feedItem);
        }
    }

    public static class FeedItem {

        public String id_actualite;
        public String titre;
        public String description;
        public String date;
        public String url_image;
        public String url_news_image;
        public String url_video;


        public FeedItem(String id_actualite, String titre, String description, String date, String url_news_image, String url_image, String url_video) {

            this.id_actualite = id_actualite;
            this.titre = titre;
            this.description = description;
            this.date = date;
            this.url_image = url_image;
            this.url_news_image = url_news_image;
            this.url_video = url_video;
        }
    }



}
