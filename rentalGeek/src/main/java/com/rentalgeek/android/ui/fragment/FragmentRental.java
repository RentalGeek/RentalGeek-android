package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AppliedEvent;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.bus.events.SelectStarEvent;
import com.rentalgeek.android.bus.events.ShowPropertyPhotosEvent;
import com.rentalgeek.android.bus.events.ShowRentalEvent;
import com.rentalgeek.android.bus.events.UnSelectStarEvent;
import com.rentalgeek.android.mvp.common.StarView;
import com.rentalgeek.android.mvp.rental.RentalPresenter;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.pojos.PhotoDTO;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.activity.ActivityPropertyPhoto;
import com.rentalgeek.android.utils.ResponseParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.rentalgeek.android.constants.IntentKey.*;

public class FragmentRental extends GeekBaseFragment implements RentalView, StarView {

    @InjectView(R.id.price) TextView price_textview;
    @InjectView(R.id.rental_image) ImageView rental_imageview;
    @InjectView(R.id.room_count) TextView room_count_textview;
    @InjectView(R.id.address) TextView address_textview;
    @InjectView(R.id.star_image) ImageView star_imageview;
    @InjectView(R.id.description) TextView description_textview;
    @InjectView(R.id.amenities) TextView amenities_textview;
    @InjectView(R.id.apply_btn) Button apply_btn;
    @InjectView(R.id.property_photo_gallery) LinearLayout propertyPhotoGallery;
    @InjectView(R.id.view_below_image) LinearLayout viewBelowImage;

    private RentalPresenter presenter;
    private boolean fullView = false;
    private String rental_id;
    private final ArrayList<String> photoUrls = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new RentalPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_rental, container, false);
        ButterKnife.inject(this, view);

        if (!fullView) {
            viewBelowImage.setVisibility(View.GONE);
            hide();

            //PreDraw guarantees measuring of screen size.
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getView().getViewTreeObserver().removeOnPreDrawListener(this);

                    float imgHeight = getActivity().getResources().getDimension(R.dimen.img_height);
                    ViewGroup.LayoutParams params = getView().getLayoutParams();
                    params.height = (int) imgHeight;
                    getView().setLayoutParams(params);

                    return true;
                }
            });
        }

        if (getArguments() != null) {
            Bundle args = getArguments();
            rental_id = args.getString(RENTAL_ID);
        }

        presenter.getPropertyPhotos(rental_id);

        return view;
    }

    public void onEventMainThread(AppliedEvent event) {
        apply_btn.setText(SessionManager.Instance.applyApproveButtonText());
        apply_btn.setClickable(false);
    }

    public void setFullView(boolean fullView) {
        this.fullView = fullView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            Bundle args = getArguments();
            rental_id = args.getString(RENTAL_ID);
        }

        presenter.getRental(rental_id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void show() {
        getFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.abc_slide_in_bottom, 0).
                show(this).
                commit();
    }

    @Override
    public void hide() {
        getFragmentManager().
                beginTransaction().
                setCustomAnimations(0, R.anim.abc_slide_out_bottom).
                hide(this).
                commit();
    }

    public void onEventMainThread(ShowRentalEvent event) {
        if (event == null || event.getRental() == null) {
            return;
        }

        Rental rental = event.getRental();

        rental_id = rental.getId();
        star_imageview.setTag(rental.getId());
        price_textview.setText(String.format("$%d", rental.getMonthlyRent()));
        room_count_textview.setText(String.format("%d BR, %d Bath", rental.getBedroomCount(), rental.getBathroomCount()));
        address_textview.setText(String.format("%s\n%s, %s %s", rental.getAddress(), rental.getCity(), rental.getState(), rental.getZipcode()));
        description_textview.setText(rental.getDescription());

        if (SessionManager.Instance.getCurrentUser().is_cosigner) {
            apply_btn.setText("APPROVE");
        }

        if (rental.applied()) {
            apply_btn.setText(SessionManager.Instance.applyApproveButtonText());
            apply_btn.setClickable(false);
        }

        StringBuilder amenities = new StringBuilder();

        for (String amenity : rental.getAmenities()) {
            amenity = ResponseParser.humanize(amenity);
            amenities.append(String.format("\u2022 %s\n", amenity));
        }

        amenities_textview.setText(amenities);

        Picasso.with(getActivity())
            .load(rental.getImageUrl())
            .into(rental_imageview);

        if (rental.isStarred()) {
            selectStar();
        } else {
            unselectStar();
        }

        show();
    }

    public void onEventMainThread(ShowPropertyPhotosEvent event) {
        if (event == null || event.getPropertyPhotos() == null) {
            return;
        }

        ArrayList<PhotoDTO> propertyPhotos = event.getPropertyPhotos();

        for (PhotoDTO photoDTO : propertyPhotos) {
            photoUrls.add(photoDTO.photo);
        }

        for (int i = 0; i < propertyPhotos.size(); i++) {
            final PhotoDTO photoInfo = propertyPhotos.get(i);
            final ImageView imageView = new ImageView(getActivity());
            propertyPhotoGallery.addView(imageView);
            imageView.setPadding(4, 0, 4, 0);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.loading_gray_image_bg));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            float imgHeight = getActivity().getResources().getDimension(R.dimen.thumb_img_height);
            imageView.getLayoutParams().width = (int) imgHeight;
            imageView.getLayoutParams().height = (int) imgHeight;
            Picasso.with(getActivity()).load(photoInfo.thumb).into(imageView);

            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhotoGallery(finalI);
                }
            });
        }
    }

    @OnClick(R.id.rental_image)
    void onRentalClick() {
        if (!fullView) {
            Bundle bundle = new Bundle();
            bundle.putString(RENTAL_ID, rental_id);
            AppEventBus.post(new ClickRentalEvent(bundle));
        } else {
            openPhotoGallery(0);
        }
    }

    private void openPhotoGallery(int originalPhotoIndex) {
        Intent intent = new Intent(getActivity(), ActivityPropertyPhoto.class);
        intent.putExtra(PHOTO_URLS, photoUrls);
        intent.putExtra(ORIGINAL_POSITION, originalPhotoIndex);
        getActivity().startActivity(intent);
    }

    @Override
    public void selectStar() {
        if (rental_imageview != null) {
            Picasso.with(getActivity())
                .load(R.drawable.star_full)
                .into(star_imageview);
        }
    }

    @Override
    public void unselectStar() {
        if (rental_imageview != null) {
            Picasso.with(getActivity())
                .load(R.drawable.star_outline)
                .into(star_imageview);
        }
    }

    @OnClick(R.id.star_image)
    public void star(View view) {
        presenter.select(rental_id);
    }

    @OnClick(R.id.apply_btn)
    public void onApplyClick() {
        presenter.apply(rental_id);
    }

    public void onEventMainThread(SelectStarEvent event) {
        selectStar();
    }

    public void onEventMainThread(UnSelectStarEvent event) {
        unselectStar();
    }

}
