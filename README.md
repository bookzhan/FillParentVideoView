#### Let VideoView fill the screen/parent layout and ensure no distortion

​	It is a good choice to simply play video in Android with VideoView. Various manufacturers generally test video playback at the factory. This compatibility is generally guaranteed, and using VideoView playback will automatically correct the video angle, but there are One problem is that VideoView always has some blank areas left during use. Many times we want him to fill the screen, or the parent layout, so we have today’s topic (of course, you can also use OpenGL to draw the video).

​	First of all, there is a pit when VideoView is used, that is, the video address must be given to it during initialization, otherwise the video is distorted, because the size information of the video is not obtained during the layout, so it is not known during initialization How much Surface to initialize, this determines two ways to use VideoView:

1. When placed in xml, then you need to pass in the video address during Activity onCreate.
2. The dynamic new VideoView() is then added to the specified parent control, and the video address also needs to be passed in

The principle is as follows:

1. Get the accurate video width and height after getting the video address
2. In onSizeChanged, calculate the layout parameters of VideoView according to the width and height of the video and the width and height of the parent layout, and use the -margin to display the extra area of the video to the outside of the control, so as to fill the parent control
3. Rewrite the onMeasure method to override the logic of VideoView, and then enjoy it



[Chinese DOC(中文文档)](./README_CN.md)

