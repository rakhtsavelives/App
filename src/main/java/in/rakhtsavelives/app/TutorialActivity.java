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
        addSlide(AppIntroFragment.newInstance("Welcome", "This App can SAVE LIVES", R.drawable.splash_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Welcome", "This App can SAVE LIVES", R.drawable.emergency_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));
        addSlide(AppIntroFragment.newInstance("Welcome", "This App can SAVE LIVES", R.drawable.emergency_button_screen_shot, getResources().getColor(R.color.tutorial_slide_background)));

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
