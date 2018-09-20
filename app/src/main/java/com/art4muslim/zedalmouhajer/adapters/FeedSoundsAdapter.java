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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.models.News;
import com.art4muslim.zedalmouhajer.models.Sound;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.MyWebViewClient;
import com.art4muslim.zedalmouhajer.view.LoadingFeedItemView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaoutherbouguerra on 05.09.18.
 */
public class FeedSoundsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


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
    private ArrayList<Sound> sounds = new ArrayList<>();
  //  private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;

    public FeedSoundsAdapter(Context context, ArrayList<Sound> actualiteArrayList) {
        this.context = context;
        this.sounds = actualiteArrayList;
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

    public void updateItems(boolean animated, ArrayList<Sound> actualiteArrayList) {
        feedItems.clear();

        for (Sound sound: actualiteArrayList){
            feedItems.add(new FeedItem( sound.getId(), sound.getName(), sound.getSoundcloud(),
                    sound.getDate() ));
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



        @BindView(R.id.ivUserProfile)
        ImageView ivUserProfile;

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


        @BindView(R.id.ivTitle)
        TextView ivTitle;


        @BindView(R.id.webView)
        WebView webView;

        FeedItem feedItem;

        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(final FeedItem feedItem) {
            this.feedItem = feedItem;

            Log.e("Feed adapter"," titre = "+feedItem.titre);
            Log.e("Feed adapter"," date = "+feedItem.date);
            Log.e("Feed adapter"," soundcloud = "+feedItem.soundcloud);

            webView.setWebViewClient(new MyWebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(feedItem.soundcloud, "text/html", null);
            ivTitle.setText(feedItem.titre);

            ivUserProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.microphone));

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



}
