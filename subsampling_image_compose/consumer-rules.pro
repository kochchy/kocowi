#-keep class com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
#-keep class com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.** {*;}
#-keep class com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView {
#    public <methods>;
#}

-keepclassmembers class ** {
   public static *** SubsamplingImage(***);
}