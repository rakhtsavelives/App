package in.rakhtsavelives.app;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class TutorialActivity extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Welcome", "An App For Mankind", R.drawable.tutorial_01, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Emergency Screen", "Many Solutions at One Place", R.drawable.tutorial_02, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Emergency Button", "Life Saviour In Emergency", R.drawable.tutorial_03, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Select blood group of blood needed", "", R.drawable.tutorial_04, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Everyone around you having same blood group will be notified", "Your work is done.\nNow, our app will work to get you a donor.", R.drawable.tutorial_05, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Donor within same location and with same blood group will be notified.", "", R.drawable.tutorial_06, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Person available for donating will accept the request", "We also confirm that he has not donated blood within last 3 months", R.drawable.tutorial_07, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Contact information of the donor will be shared with you", " Also, donner will be shared with your information", R.drawable.tutorial_08, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Profile", "Your Confidential Profile", R.drawable.tutorial_09, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Availability across all groups", "We have included almost all Blood Groups available\n(Including Bombay Blood Group(HH))", R.drawable.tutorial_10, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Search", "Search each drop of blood across the Nation", R.drawable.tutorial_11, getResources().getColor(R.color.tutorial_slide_background)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        // setBarColor(Color.parseColor("#3F51B5"));
        // setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        // showSkipButton(false);
        // setProgressButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        finish();
    }

    @Override
    public void onSlideChanged() {

    }
}
