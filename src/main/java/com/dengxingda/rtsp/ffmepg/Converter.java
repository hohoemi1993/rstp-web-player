package com.dengxingda.rtsp.ffmepg;

import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_MPEG1VIDEO;

import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacv.*;

/**
 * Created on 2019/1/31<br>
 *
 * @author dengxingda
 * @version 1.0
 */
public class Converter {

    private FFmpegFrameGrabber grabber;

    private FFmpegFrameRecorder recorder;

    private int width = -1;

    private int height = -1;

    // video params
    private int audioCodecId;
    private int codecId;
    private double frameRate;
    private int bitRate;

    // audio params
    // record audio need ：audioChannels > 0 && audioBitrate > 0 && sampleRate > 0
    private int audioChannels;
    private int audioBitrate;
    private int sampleRate;

    // context
    private avformat.AVFormatContext formatContext;

    private Converter grab() throws FrameGrabber.Exception, FrameRecorder.Exception {

        grabber = new FFmpegFrameGrabber("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");

        grabber.setOption("rtsp_transport", "tcp");

        // may throw a FrameGrabber.Exception
        grabber.start();

        if (width < 0 || height < 0) {
            width = grabber.getImageWidth();
            height = grabber.getImageHeight();
        }

        audioCodecId = grabber.getAudioCodec();
        codecId = grabber.getVideoCodec();
        frameRate = grabber.getVideoFrameRate();
        bitRate = grabber.getVideoBitrate();
        audioChannels = grabber.getAudioChannels();
        audioBitrate = grabber.getAudioBitrate();
        if (audioBitrate < 1) {
            // audio bitrate of default
            audioBitrate = 128 * 1000;
        }
        sampleRate = grabber.getSampleRate();

        formatContext = grabber.getFormatContext();

        recorder = new FFmpegFrameRecorder("udp://localhost:15000",width,height);
        recorder.setFormat("mpegts");
        /*
            crf for video compression
            0 means no compression
            value range 0-18 is good
                        18-28 not bad
                        above 28 bad
          */
        recorder.setVideoOption("crf", "0");
//        recorder.setGopSize(10);
        recorder.setFrameRate(frameRate);
        recorder.setVideoBitrate(720000);

        recorder.setAudioChannels(audioChannels);
        recorder.setAudioBitrate(audioBitrate);
        recorder.setSampleRate(sampleRate);

        //use h264 and aac to encode
        // AV_CODEC_ID_H264
        recorder.setVideoCodec(AV_CODEC_ID_MPEG1VIDEO);
        recorder.setAudioCodec(audioCodecId);

        // may throw a FrameGrabber.Exception
        // AVPacket need this AVFormatContext
        recorder.start(null);
        return this;
    }

    public void push() throws Exception {
        Frame frame = null;
        while (null != (frame = grabber.grabImage())) {
            // the monitor video is 30 fps
            recorder.record(frame);
        }
    }

    public static void main(String[] args) throws Exception {
        Converter converter = new Converter();
        converter.grab().push();
    }
}
