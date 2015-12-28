package in.rakhtsavelives.app;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class TutorialActivity extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Welcome", "An App For Mankind", R.drawable.splash_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Emergency Screen", "Many Solutions at One Place", R.drawable.emergency_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Emergency Button", "Life Saviour In Emergency", R.drawable.emergency_button_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Profile", "Your Confidential Profile", R.drawable.profile_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Availability across all groups", "We have included almost all Blood Groups available\n(Including Bombay Blood Group(HH))", R.drawable.bg_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Search", "Search each drop of blood across the Nation", R.drawable.search_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));

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
