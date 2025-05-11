package upworksolutions.themagictricks.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MysticalAnimationUtils {
    
    public static void startFloatingAnimation(View view) {
        // Create vertical floating animation
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", 0f, -20f);
        translateY.setDuration(2000);
        translateY.setRepeatCount(ObjectAnimator.INFINITE);
        translateY.setRepeatMode(ObjectAnimator.REVERSE);
        
        // Create rotation animation
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 5f);
        rotate.setDuration(3000);
        rotate.setRepeatCount(ObjectAnimator.INFINITE);
        rotate.setRepeatMode(ObjectAnimator.REVERSE);
        
        // Create alpha animation
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.6f, 1f);
        alpha.setDuration(2000);
        alpha.setRepeatCount(ObjectAnimator.INFINITE);
        alpha.setRepeatMode(ObjectAnimator.REVERSE);
        
        // Combine animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateY, rotate, alpha);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }
    
    public static void startOrbAnimation(View orb) {
        // Create floating animation
        ObjectAnimator translateY = ObjectAnimator.ofFloat(orb, "translationY", 0f, -30f);
        translateY.setDuration(3000);
        translateY.setRepeatCount(ObjectAnimator.INFINITE);
        translateY.setRepeatMode(ObjectAnimator.REVERSE);
        
        // Create scale animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(orb, "scaleX", 1f, 1.2f);
        scaleX.setDuration(2000);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(orb, "scaleY", 1f, 1.2f);
        scaleY.setDuration(2000);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        
        // Combine animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateY, scaleX, scaleY);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }
} 