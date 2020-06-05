package bz.luoye.fillparentvideoview;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.VideoView;


/**
 * Created by zhandalin on 2020-06-05 09:13.
 * description:
 */
public class FillParentVideoView extends VideoView {
    private static final String TAG = "FillParentVideoView";
    private int videoWidth = 0;
    private int videoHeight = 0;
    private int videoRotation = 0;
    private boolean fullParentView = true;

    public FillParentVideoView(Context context) {
        this(context, null);
    }

    public FillParentVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillParentVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (videoWidth > 0 && videoHeight > 0 && fullParentView) {
            ViewGroup parent = (ViewGroup) getParent();
            w = parent.getWidth();
            h = parent.getHeight();
            Log.d(TAG, "onSizeChanged w=" + w + " h=" + h);
            float videoRatio = videoWidth * 1.0f / videoHeight;
            float viewRatio = w * 1.0f / h;
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (viewRatio > videoRatio) {
                layoutParams.width = w;
                layoutParams.height = (int) (w / videoRatio + 0.5f);
            } else {
                layoutParams.width = (int) (h * videoRatio + 0.5f);
                layoutParams.height = h;
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                if (layoutParams.width != w) {
                    marginLayoutParams.leftMargin = (w - layoutParams.width) / 2;
                    marginLayoutParams.rightMargin = (w - layoutParams.width) / 2;
                }
                if (layoutParams.height != h) {
                    marginLayoutParams.topMargin = (h - layoutParams.height) / 2;
                    marginLayoutParams.bottomMargin = (h - layoutParams.height) / 2;
                }
                Log.d(TAG, "leftMargin=" + marginLayoutParams.leftMargin + " rightMargin=" + marginLayoutParams.rightMargin + " topMargin=" + marginLayoutParams.topMargin + " bottomMargin=" + marginLayoutParams.bottomMargin);
            }
            setLayoutParams(layoutParams);
            Log.d(TAG, "layoutParams width=" + layoutParams.width + " height=" + layoutParams.height);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout " + changed + " left=" + left + " top=" + top + " right=" + right + " bottom=" + bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (fullParentView) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void setVideoPath(String path) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            String widthString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String heightString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String rotationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            Log.d(TAG, "video width=" + widthString + " height=" + heightString + " rotation=" + rotationString);

            videoWidth = Integer.parseInt(widthString);
            videoHeight = Integer.parseInt(heightString);
            int tempRotation = Integer.parseInt(rotationString);
            videoRotation = (tempRotation % 360 + 360) % 360;
            if (videoRotation == 90 || videoRotation == 270) {
                int temp = videoWidth;
                videoWidth = videoHeight;
                videoHeight = temp;
            }
            Log.d(TAG, "videoRotation=" + videoRotation + " videoWidth=" + videoWidth + " videoHeight=" + videoHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setVideoPath(path);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        setLayoutParams(layoutParams);
    }

    public boolean isFullParentView() {
        return fullParentView;
    }

    public void setFullParentView(boolean fullParentView) {
        this.fullParentView = fullParentView;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoRotation() {
        return videoRotation;
    }

    public void setVideoRotation(int videoRotation) {
        this.videoRotation = videoRotation;
    }
}
