package com.example.paintthetown491;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewPopUp extends Activity
{
    private TextView rev_date;
    private TextView review;
    private RatingBar rev_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.review_pop_up);

        //binds the xml
        review=findViewById(R.id.review_popup_review);
        rev_date=findViewById(R.id.review_popup_date);

        LocationReview review=(LocationReview) getIntent().getExtras().getSerializable("review");

        this.review.setText(review.getReview());
        this.rev_date.setText(review.getDate());

        //closes the activity when you click outside
        this.setFinishOnTouchOutside(true);

        //will allow us to set the size of the popup relative to the screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int w = metrics.widthPixels;
        int h = metrics.heightPixels;

        //1 indicates to make the popup 100% the size of the screen
        getWindow().setLayout((int) (w * 1), (int) (h * 1));
    }
}
