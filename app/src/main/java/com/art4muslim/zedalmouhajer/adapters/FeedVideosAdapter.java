package com.art4muslim.zedalmouhajer.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.models.Sound;
import com.art4muslim.zedalmouhajer.models.Video;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.MyWebViewClient;
import com.art4muslim.zedalmouhajer.view.LoadingFeedItemView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaoutherbouguerra on 05.09.18.
 */
public class FeedVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;
    public static String Facebook = "com.facebook.katana";
    public static String Twitter = "com.twitter.android";
    public static String Instagram = "com.instagram.android";
    public static String Whatsup = "com.whatsapp";
    public static String GooglePlus = "com.google.android.apps.plus";
    private final List<FeedItem> feedItems = new ArrayList<>();

    public static Context context;
    BaseApplication app;
    private ArrayList<Video> videos = new ArrayList<>();
  //  private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;

    public FeedVideosAdapter(Context context, ArrayList<Video> actualiteArrayList) {
        this.context = context;
        this.videos = actualiteArrayList;
        app = (BaseApplication) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEFAULT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_feed_sounds, parent, false);
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

    public void updateItems(boolean animated, ArrayList<Video> actualiteArrayList) {
        feedItems.clear();

        for (Video video: actualiteArrayList) {
            feedItems.add(new FeedItem( video.getId(), video.getName(), video.getIdvideo(),
                    video.getDate() ));
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

        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView youtubeView;

        @BindView(R.id.relativeLayout_over_youtube_thumbnail)
        RelativeLayout relativeLayoutOverYoutubeThumbnail;

        @BindView(R.id.ivUserProfile)
        ImageView ivUserProfile;

        @BindView(R.id.cardView)
        CardView vVideoRoot;

        @BindView(R.id.btnYoutube_player)
        ImageView playBtn;

        @BindView(R.id.ivTitle)
        TextView ivTitle;


        @BindView(R.id.webView)
        WebView webView;


        @BindView(R.id.img_gplus)
        ImageView img_gplus;

        @BindView(R.id.img_whats)
        ImageView img_whats;

        @BindView(R.id.img_insta)
        ImageView img_insta;

        @BindView(R.id.img_twitter)
        ImageView img_twitter;

        @BindView(R.id.img_fcb)
        ImageView img_fcb;



        FeedItem feedItem;

        private static final int RECOVERY_REQUEST = 1;
        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(final FeedItem feedItem) {
            this.feedItem = feedItem;
            int adapterPosition = getAdapterPosition();


            Log.e("Feed adapter"," titre = "+feedItem.titre);
            Log.e("Feed adapter"," date = "+feedItem.date);
            Log.e("Feed adapter"," soundcloud = "+feedItem.soundcloud);
            webView.setVisibility(View.GONE);
            ivUserProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.camera));

            if (feedItem.soundcloud != null){
                vVideoRoot.setVisibility(View.VISIBLE);

                //    youtubeView.initialize(Constants.YOUTUBE_API_KEY, this);

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

                        youTubeThumbnailLoader.setVideo(feedItem.soundcloud);

                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);

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
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, Constants.YOUTUBE_API_KEY, "P3mAtvs5Elc");
                        context.startActivity(intent);
                    }
                });


                img_fcb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        shareOn(Facebook, feedItem.titre, feedItem.soundcloud);
                    }
                });
                img_twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareOn(Twitter, feedItem.titre, feedItem.soundcloud);
                    }
                });
                img_insta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareOn(Instagram, feedItem.titre, feedItem.soundcloud);
                    }
                });
                img_whats.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareOn(Whatsup, feedItem.titre, feedItem.soundcloud);
                    }
                });
                img_gplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareOn(GooglePlus, feedItem.titre, feedItem.soundcloud);
                    }
                });

            }


            ivTitle.setText(feedItem.titre);

            //ivFeedBottom.setImageResource(adapterPosition % 2 == 0 ? R.mipmap.img_feed_bottom_1 : R.mipmap.img_feed_bottom_2);

        }

        public FeedItem getFeedItem() {
            return feedItem;
        }


        private void shareOn(String application, String title, String description){


            try{

                Intent intent = context.getPackageManager().getLaunchIntentForPackage(application);
                if (intent != null) {
                    // The application exists
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    //  shareIntent.setPackage(application);
                    shareIntent.setType("message/rfc822");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TITLE, title);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, description);
                    // Start the specific social application
                    context.startActivity(Intent.createChooser(shareIntent,
                            " : "));
                } else {
                    Toast.makeText(context,"هذا التطبيق غير موجود على جوالك" , Toast.LENGTH_LONG).show();
                    // The application does not exist
                    // Open GooglePlay or use the default system picker
                }

            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
// show message to user
            }
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
        public String soundcloud;
        public String date;



        public FeedItem(String id_song, String titre, String soundcloud, String date) {

            this.id_actualite = id_song;
            this.titre = titre;
            this.soundcloud = soundcloud;
            this.date = date;

        }
    }

  /*  public interface OnFeedItemClickListener {
        void onCommentsClick(View v, int position);

        void onMoreClick(View v, int position);

        void onProfileClick(View v);
    }
    */
}
