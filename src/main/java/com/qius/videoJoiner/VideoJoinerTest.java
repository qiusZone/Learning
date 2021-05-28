package com.qius.videoJoiner;

import com.coremedia.iso.boxes.Container;
import com.google.common.collect.Lists;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/5/13.
 * @see [相关类/方法]
 * @since VideoJoinerTest 1.0
 */
public class VideoJoinerTest {

    public static void main(String[] args) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("E:\\temp\\video\\245\\4d6155c1480742c4b86e7f3326ecac56.mp4");
        list.add("E:\\temp\\video\\245\\e7429b1fe95c42dba416fc5a43dba01e.mp4");
        final File file = new File("E:\\temp\\video\\245\\123.mp4");
        mergeVideo(list, file);
    }

    public static String mergeVideo(List<String> videoList, File mergeVideoFile) {
        FileOutputStream fos = null;
        FileChannel fc = null;
        try {
            List<Movie> sourceMovies = new ArrayList<>();
            for (String video : videoList) {
                sourceMovies.add(MovieCreator.build(video));
            }

            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();

            for (Movie movie : sourceMovies) {
                for (Track track : movie.getTracks()) {
                    if ("soun".equals(track.getHandler())) {
                        audioTracks.add(track);
                    }

                    if ("vide".equals(track.getHandler())) {
                        videoTracks.add(track);
                    }
                }
            }

            Movie mergeMovie = new Movie();
            if (audioTracks.size() > 0) {
                mergeMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }

            if (videoTracks.size() > 0) {
                mergeMovie.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }

            Container out = new DefaultMp4Builder().build(mergeMovie);
            fos = new FileOutputStream(mergeVideoFile);
            fc = fos.getChannel();
            out.writeContainer(fc);
            fc.close();
            fos.close();
            return mergeVideoFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
