package com.screenmirror.screentv.tvsharingapp.ExtraClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.screenmirror.screentv.tvsharingapp.Activity.Playlist.Adapter.PlaylistAdapter;
import com.screenmirror.screentv.tvsharingapp.Model.Folder;
import com.screenmirror.screentv.tvsharingapp.Model.ItemPlaylist;
import com.screenmirror.screentv.tvsharingapp.Model.Playlist;
import com.screenmirror.screentv.tvsharingapp.Model.Song;
import com.screenmirror.screentv.tvsharingapp.Model.VideoFolder;
import com.screenmirror.screentv.tvsharingapp.Model.VideoModel;
import com.screenmirror.screentv.tvsharingapp.Model.WhatsappStatusModel;
import com.screenmirror.screentv.tvsharingapp.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GetData {

    public static String foldername = "foldername";
    public static String videolist = "videolist";
    public static String songlist = "songlist";
    public static String position = "position";
    public static String data = "data";
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/HDVideoplayer/WAstatus");
    public static boolean isDelete = false;
    public static int max_size_recentlist = 20;

    public static String PlayName = "";
    public static List<ItemPlaylist> itemPlaylistList = new ArrayList<>();

    public static List<VideoFolder> getVideoFolderList(Activity activity) {
        List<VideoFolder> videoFolderList = new ArrayList<>();

        List<VideoModel> videoList = new ArrayList<>();
        videoList = getVideoList(activity);

        for (int i = 0; i < videoList.size(); i++) {

            VideoFolder videoFolder = new VideoFolder(videoList.get(i).getFolderid(), "", -1, "", "", new ArrayList<>());

            if (videoFolderList.contains(videoFolder)) {
                videoFolderList.get(videoFolderList.indexOf(videoFolder)).getVideoList().add(videoList.get(i));
                videoFolderList.get(videoFolderList.indexOf(videoFolder)).setTotalvideo(videoFolderList.get(videoFolderList.indexOf(videoFolder)).getVideoList().size());

            } else {
                List<VideoModel> videoList1 = new ArrayList<>();
                videoList1.add(videoList.get(i));
                if (videoList.get(i).getFoldername() == null) {
                    videoList.get(i).setFoldername("Unknow Folder");
                }
                VideoFolder videoFolder1 = new VideoFolder(videoList.get(i).getFolderid(), videoList.get(i).getFoldername(), 1, videoList.get(i).getStr_path(), videoList.get(i).getStr_thumb(), videoList1);
                videoFolderList.add(videoFolder1);
            }
        }

        return videoFolderList;
    }

    public static List<VideoModel> getVideoList(Activity activity) {
        List<VideoModel> videoList = new ArrayList<>();

        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_id, thum, duration, folderid, title, filepath;

        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        folderid = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        title = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
        duration = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
        filepath = cursor.getColumnIndex(MediaStore.Video.Media.DATA);


        if (cursor != null && cursor.moveToFirst()) {

            do {

                int videoid = cursor.getInt(column_id);
                String videotitle = cursor.getString(title);
                int videiduration = cursor.getInt(duration);
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String foldername = cursor.getString(column_index_folder_name);
                int folder_id = cursor.getInt(folderid);
                String thumnail = cursor.getString(thum);
                absolutePathOfImage = cursor.getString(column_index_data);
                String filepath_ = cursor.getString(filepath);
                String Resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                String date = null;
                try {
                    date = convertDate(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)), "dd/MM/yyyy");
                } catch (Exception e) {
                    e.printStackTrace();
                    date = "0 Mb";
                }

                File file = new File(filepath_);
                long fileSize = 0;
                if (file.exists()) {
                    fileSize = file.length();
                }

                VideoModel video = new VideoModel(videoid, videotitle, videiduration, data, foldername,
                        folder_id, absolutePathOfImage, thumnail, filepath_, Resolution, date, getStringSizeLengthFile(fileSize));

                videoList.add(video);

            } while (cursor.moveToNext());

        }
        return videoList;
    }

    public static List<VideoModel> getwhatsappdownload(Activity activity) {
        List<VideoModel> videoList = new ArrayList<>();

        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_id, thum, duration, folderid, title, filepath;

        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        String selection = MediaStore.Video.Media.DATA + " like?";
        cursor = activity.getContentResolver().query(uri, projection, selection, new String[]{"%WAstatus%"}, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        folderid = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        title = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
        duration = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
        filepath = cursor.getColumnIndex(MediaStore.Video.Media.DATA);


        if (cursor != null && cursor.moveToFirst()) {

            do {

                int videoid = cursor.getInt(column_id);
                String videotitle = cursor.getString(title);
                int videiduration = cursor.getInt(duration);
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String foldername = cursor.getString(column_index_folder_name);
                int folder_id = cursor.getInt(folderid);
                String thumnail = cursor.getString(thum);
                absolutePathOfImage = cursor.getString(column_index_data);
                String filepath_ = cursor.getString(filepath);
                String Resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                String date = null;
                try {
                    date = convertDate(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)), "dd/MM/yyyy");
                } catch (Exception e) {
                    e.printStackTrace();
                    date = "0 Mb";
                }

                File file = new File(filepath_);
                long fileSize = 0;
                if (file.exists()) {
                    fileSize = file.length();
                }

                VideoModel video = new VideoModel(videoid, videotitle, videiduration, data, foldername,
                        folder_id, absolutePathOfImage, thumnail, filepath_, Resolution, date, getStringSizeLengthFile(fileSize));

                videoList.add(video);

            } while (cursor.moveToNext());

        }
        return videoList;
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static String timeConversion(int value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }

    public static String getFileExtension(String fileName) {
        String fileNameArray[] = fileName.split("\\.");
        return fileNameArray[fileNameArray.length - 1];


    }

    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if (size < sizeMb)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }

    public static void shareVideo(Context context2, String str) {
        Uri parse = Uri.parse(str);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("video/mp4");
        intent.putExtra("android.intent.extra.SUBJECT", context2.getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", context2.getString(R.string.share_app_message) + ("\nhttps://play.google.com/store/apps/details?id=" + context2.getPackageName()));
        intent.putExtra("android.intent.extra.STREAM", parse);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context2.startActivity(Intent.createChooser(intent, "Share Video using"));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(context2, R.string.no_app_installed, Toast.LENGTH_LONG).show();
        }
    }


    public static List<Song> getSongList(Activity activity) {

        HashMap<String, String> albumartData = new HashMap<>();

        String[] projection1 = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        ContentResolver content = activity.getContentResolver();
        @SuppressLint("Recycle") Cursor albumArtCursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection1,
                null,
                null, //new String[]{songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))},
                null);

        while (Objects.requireNonNull(albumArtCursor).moveToNext()) {
            albumartData.put(albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)),
                    albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
            //   Log.d(TAG,albumartData.get(AlbumArtCursor.getString(AlbumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)))+"Albumdata");
        }

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//        String selection = MediaStore.Audio.Media.DATA;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.DATA,
        };

        ContentResolver contentsong = activity.getContentResolver();
        Cursor song_cursor = contentsong.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        List<Song> songList = new ArrayList<>();


        if (song_cursor != null && song_cursor.moveToFirst()) {
            do {
                final String title, artist, album, albumart, path, albumid;
                final long _id, time;
                final int seconds, min, trackid, yr, duration, id, songid;

                String GENRE_ID = MediaStore.Audio.Genres._ID;
                String GENRE_NAME = MediaStore.Audio.Genres.NAME;
                String SONG_ID = MediaStore.Audio.Media._ID;
                String SONG_TITLE = MediaStore.Audio.Media.TITLE;
                String SONG_ARTIST = MediaStore.Audio.Media.ARTIST;
                String SONG_ALBUM = MediaStore.Audio.Media.ALBUM;
                String SONG_YEAR = MediaStore.Audio.Media.YEAR;
                String SONG_TRACK_NO = MediaStore.Audio.Media.TRACK;
                String SONG_FILEPATH = MediaStore.Audio.Media.DATA;
                String SONG_DURATION = MediaStore.Audio.Media.DURATION;
                String ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID;


                songid = song_cursor.getInt(song_cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                title = song_cursor.getString(song_cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                artist = song_cursor.getString(song_cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                album = song_cursor.getString(song_cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                time = song_cursor.getLong(song_cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                trackid = song_cursor.getInt(song_cursor.getColumnIndex(SONG_TRACK_NO));
                yr = song_cursor.getInt(song_cursor.getColumnIndex(SONG_YEAR));
                duration = song_cursor.getInt(song_cursor.getColumnIndex(SONG_DURATION));
                id = song_cursor.getInt(song_cursor.getColumnIndex(ARTIST_ID));
                path = song_cursor.getString(song_cursor.getColumnIndex(SONG_FILEPATH));
                albumid = song_cursor.getString(song_cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                albumart = albumartData.get(song_cursor.getString(song_cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))) + "";


                Song song = new Song(songid, title, trackid, yr, duration, path, album, id, artist, albumart, albumid);

                songList.add(song);


            } while (song_cursor.moveToNext());
        }

        return songList;
    }

    public static List<Folder> getFolderList(Activity activity) {

        List<Folder> folderList = new ArrayList<>();


        List<Song> songList = getSongList(activity);

        for (int i = 0; i < songList.size(); i++) {
            String name = new File(songList.get(i).getSongPath()).getParentFile().getName();
            Folder folder = new Folder(name, 0, "", new ArrayList<>());


            if (folderList.contains(folder)) {
                folderList.get(folderList.indexOf(folder)).getSongList().add(songList.get(i));
                folderList.get(folderList.indexOf(folder)).setSongcount(folderList.get(folderList.indexOf(folder)).getSongList().size());
            } else {

                List<Song> songList1 = new ArrayList<>();
                songList1.add(songList.get(i));
                Folder folder1 = new Folder(name, 1, songList.get(i).getSongPath(), songList1);
                folderList.add(folder1);
            }


        }

//        folderList.add(new Folder("Hidden Folder", -1, "", new ArrayList<>()));

//        Toast.makeText(getContext(), ""+folderList.size(), Toast.LENGTH_SHORT).show();

        return folderList;

    }


    public static Song getRandomAudio(List<Song> audioList) {
        Random random = new Random();
        return audioList.get(random.nextInt(audioList.size()));
    }


    public static void createFileFolder() {

        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }

    }

    public static void shareImageAndVideo(Context context2, String str, boolean z) {
        Uri parse = Uri.parse(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setPackage("com.whatsapp");
        intent.putExtra("android.intent.extra.TEXT", context2.getString(R.string.share_app_message) + ("\nhttps://play.google.com/store/apps/details?id=" + context2.getPackageName()));
        intent.putExtra("android.intent.extra.STREAM", parse);
        if (z) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context2.startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(context2, "Whatsapp Not Installed", Toast.LENGTH_SHORT).show();
//            setToast(context2, context2.getResources().getString(R.string.whatsapp_not_installed));
        }
    }

    public static void shareImage(Context context2, String str) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.SUBJECT", context2.getString(R.string.app_name));
            intent.putExtra("android.intent.extra.TEXT", context2.getString(R.string.share_app_message) + ("\nhttps://play.google.com/store/apps/details?id=" + context2.getPackageName()));
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(MediaStore.Images.Media.insertImage(context2.getContentResolver(), str, "", (String) null)));
            intent.setType("image/*");
            context2.startActivity(Intent.createChooser(intent, context2.getResources().getString(R.string.share_image_via)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File[] allfiles;
    private static File[] allfilesBusiness;
    static ArrayList<WhatsappStatusModel> statusModelArrayList;

    public static int getWhatsappFileCount() {
        int wacount = 0, waBcount = 0;
        WhatsappStatusModel whatsappStatusModel;
        statusModelArrayList = new ArrayList<>();

        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp" + "/WhatsApp/Media/.Statuses";
        File targetDirector = new File(targetPath);
        allfiles = targetDirector.listFiles();

        if (targetDirector.listFiles() == null) {
            String targetPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
            File targetDirector1 = new File(targetPath1);
            allfiles = targetDirector1.listFiles();
        }

//        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp" + "/WhatsApp Business/Media/.Statuses";
        File targetDirectorBusiness = new File(targetPathBusiness);
        allfilesBusiness = targetDirectorBusiness.listFiles();

        if (targetDirectorBusiness.listFiles() == null) {
            String targetPathBusiness1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
            File targetDirectorBusiness1 = new File(targetPathBusiness1);
            allfilesBusiness = targetDirectorBusiness1.listFiles();
        }


//        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
//        File targetDirector = new File(targetPath);
//        allfiles = targetDirector.listFiles();
//
//        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
//        File targetDirectorBusiness = new File(targetPathBusiness);
//        File[] allfilesBusiness = targetDirectorBusiness.listFiles();


        try {
            Arrays.sort(allfiles, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfiles.length; i++) {
                File file = allfiles[i];
//                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                        Uri.fromFile(file),
                        allfiles[i].getAbsolutePath(),
                        file.getName());
                statusModelArrayList.add(whatsappStatusModel);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Arrays.sort(allfilesBusiness, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfilesBusiness.length; i++) {
                File file = allfilesBusiness[i];
//                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                whatsappStatusModel = new WhatsappStatusModel("WhatsStatusB: " + (i + 1),
                        Uri.fromFile(file),
                        allfilesBusiness[i].getAbsolutePath(),
                        file.getName());
                statusModelArrayList.add(whatsappStatusModel);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (statusModelArrayList.size() > 0) {
            return statusModelArrayList.size() - 1;
        } else {
            return statusModelArrayList.size();
        }


    }

    public static boolean isSameDate(String date1, String date2) {

        boolean istoday = false;

        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");

        {
            try {
                Date d1 = sdformat.parse(date1);
                Date d2 = sdformat.parse(date2);

                if (d1.compareTo(d2) > 0) {
                    istoday = false;
//                    System.out.println("Date 1 occurs after Date 2");
                } else if (d1.compareTo(d2) < 0) {
                    istoday = false;
//                    System.out.println("Date 1 occurs before Date 2");
                } else if (d1.compareTo(d2) == 0) {
                    istoday = true;
//                    System.out.println("Both dates are equal");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return istoday;
    }


    ///////////////////////////////////////////For playlist


    public static List<Playlist> GetPlaylist(Activity activity) {
        List<Playlist> fetchplaylistList = new ArrayList<>();

        Playlist playlist = new Playlist();
        playlist.setName("Create Playlist");

        fetchplaylistList.add(playlist);

       /* playlist = new Playlist();
        playlist.setName("Pratik");

        fetchplaylistList.add(playlist);*/


        String list = Preference.getplaylist();

        if (!list.isEmpty()) {
            fetchplaylistList.addAll(new Gson().fromJson(list, new TypeToken<List<Playlist>>() {
            }.getType()));
        }

        return fetchplaylistList;

    }


    public static boolean AddNameToPlaylist(Activity activity, String name) {


        String list = Preference.getplaylist();


        List<Playlist> fetchplaylistList = new ArrayList<>();
        if (!list.isEmpty()) {
            fetchplaylistList = new Gson().fromJson(list, new TypeToken<List<Playlist>>() {
            }.getType());

            Playlist playlist = new Playlist();
            playlist.setName(name);

            if (fetchplaylistList.contains(playlist)) {
                Toast.makeText(activity, "Name Already Exists", Toast.LENGTH_SHORT).show();

                return false;
            } else {
                fetchplaylistList.add(playlist);
                Preference.setplaylist(new Gson().toJson(fetchplaylistList));

                return true;
            }

        } else {
            Playlist playlist = new Playlist();
            playlist.setName(name);
            fetchplaylistList.add(playlist);

            Preference.setplaylist(new Gson().toJson(fetchplaylistList));

            return true;
        }


    }


    public static BottomSheetDialog selectplaylistdialog;

    public static void DialogSelectToplaylist(Activity activity, VideoModel videoModel, Song song) {
        selectplaylistdialog = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogTheme);
        selectplaylistdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectplaylistdialog.setContentView(R.layout.dialog_selectplaylist);
        selectplaylistdialog.setCancelable(true);
        FrameLayout bottomSheet = (FrameLayout) selectplaylistdialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);


        RecyclerView rv_playlist = selectplaylistdialog.findViewById(R.id.rv_playlist);
        rv_playlist.setLayoutManager(new LinearLayoutManager(activity));

        List<Playlist> playlists = GetData.GetPlaylist(activity);

        PlaylistAdapter playlistAdapter = new PlaylistAdapter(activity, playlists, false, videoModel, song);
        rv_playlist.setAdapter(playlistAdapter);

        selectplaylistdialog.show();

    }




    ////////////////////////////////////////////////////////


    ///////////////////////////////recent FIle


    public static void AddTorecent(VideoModel videoModel, Song song) {

        String recent = Preference.getrecentvideo();
        List<ItemPlaylist> recentitemPlaylists = new ArrayList<>();

        ItemPlaylist itemPlaylist = new ItemPlaylist();

        if (!recent.isEmpty()) {
            recentitemPlaylists = new Gson().fromJson(recent, new TypeToken<List<ItemPlaylist>>() {
            }.getType());


            if (recentitemPlaylists.size() > 0) {
                if (videoModel != null) {
                    itemPlaylist.setId(videoModel.getVideoid());
                    itemPlaylist.setVideo(true);
                    itemPlaylist.setVideoModel(videoModel);
                    itemPlaylist.setSong(null);
                } else if (song != null) {
                    itemPlaylist.setId(song.getMid());
                    itemPlaylist.setVideo(false);
                    itemPlaylist.setVideoModel(null);
                    itemPlaylist.setSong(song);
                }

                if (recentitemPlaylists.contains(itemPlaylist)) {
                    recentitemPlaylists.remove(itemPlaylist);
                    recentitemPlaylists.add(itemPlaylist);
                } else {

                    if(recentitemPlaylists.size()> max_size_recentlist){
                        recentitemPlaylists.remove(0);
                        recentitemPlaylists.add(itemPlaylist);
                    }else {
                        recentitemPlaylists.add(itemPlaylist);
                    }

                }
                Preference.setrecentvideo(new Gson().toJson(recentitemPlaylists));
            } else {
                if (videoModel != null) {
                    itemPlaylist.setId(videoModel.getVideoid());
                    itemPlaylist.setVideo(true);
                    itemPlaylist.setVideoModel(videoModel);
                    itemPlaylist.setSong(null);
                } else if (song != null) {
                    itemPlaylist.setId(song.getMid());
                    itemPlaylist.setVideo(false);
                    itemPlaylist.setVideoModel(null);
                    itemPlaylist.setSong(song);
                }
                recentitemPlaylists.add(itemPlaylist);
                Preference.setrecentvideo(new Gson().toJson(recentitemPlaylists));
            }

        } else {
            if (videoModel != null) {
                itemPlaylist.setId(videoModel.getVideoid());
                itemPlaylist.setVideo(true);
                itemPlaylist.setVideoModel(videoModel);
                itemPlaylist.setSong(null);
            } else if (song != null) {
                itemPlaylist.setId(song.getMid());
                itemPlaylist.setVideo(false);
                itemPlaylist.setVideoModel(null);
                itemPlaylist.setSong(song);
            }
            recentitemPlaylists.add(itemPlaylist);
            Preference.setrecentvideo(new Gson().toJson(recentitemPlaylists));
        }


    }


    //////////////////////////////////////


}
