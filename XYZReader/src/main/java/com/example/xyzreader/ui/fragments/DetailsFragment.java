package com.example.xyzreader.ui.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentArticleDetailsBinding;
import com.example.xyzreader.ui.model.ArticleItemViewModel;
import com.example.xyzreader.util.TransitionHelper;

/**
 * Created by lars on 20.02.17.
 */

public class DetailsFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    public static final String ARG_ITEM = "article_item";

    private static final String TAG = DetailsFragment.class.getSimpleName();

    private static final float MIN_OFFSET = 0.2F;

    public ObservableFloat alpha = new ObservableFloat();

    FragmentArticleDetailsBinding mBinding;

    public ObservableField<ArticleItemViewModel> article = new ObservableField<>();



    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(ArticleItemViewModel articleItemViewModel, String transitionName) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_ITEM, articleItemViewModel);
        arguments.putString(TransitionHelper.EXTRA_IMAGE_TRANSITION_NAME, transitionName);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_details, container, false);
        return mBinding.getRoot();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.nestedScrollview.setNestedScrollingEnabled(true);
        mBinding.fragmentAppbar.addOnOffsetChangedListener(this);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle arguments = getArguments();
        final ArticleItemViewModel article = getArguments().getParcelable(ARG_ITEM);
        final String transitionName = arguments.getString(TransitionHelper.EXTRA_IMAGE_TRANSITION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.toolbarImage.setTransitionName(transitionName);
        }
        Glide.with(getContext())
                .load(article.getPhotoUrl())
                .centerCrop()
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        showContent();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        showContent();
                        return false;
                    }
                })
                .into(mBinding.toolbarImage);
    }

    private void showContent() {
        startPostponedEnterTransition();
        mBinding.setDetailsFragment(DetailsFragment.this);
        DetailsFragment.this.article.set((ArticleItemViewModel) getArguments().getParcelable(ARG_ITEM));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.fragmentAppbar.removeOnOffsetChangedListener(this);
    }

    public void share() {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .getIntent(), getString(R.string.action_share)));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxOffset = mBinding.fragmentAppbar.getMeasuredHeight() - mBinding.toolbar.getMeasuredHeight();
        int absOffset = Math.abs(verticalOffset);
        float perOffset = absOffset / (float) maxOffset;
        if (perOffset < MIN_OFFSET) {
            perOffset = MIN_OFFSET;
        }
        alpha.set(perOffset);
    }
}
