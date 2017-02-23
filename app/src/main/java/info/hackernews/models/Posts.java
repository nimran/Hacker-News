package info.hackernews.models;

/**
 * Created by imran on 23/02/17.
 * <p>
 * HackerNews
 */

public class Posts {

    private Integer _id;
    private String _title;
    private String _author;
    private int _points;
    private int _comments;
    private long _time;
    private String _url;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public long get_time() {
        return _time;
    }

    public void set_time(long _time) {
        this._time = _time;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_author() {
        return _author;
    }

    public void set_author(String _author) {
        this._author = _author;
    }


    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public int get_points() {
        return _points;
    }

    public void set_points(int _points) {
        this._points = _points;
    }

    public int get_comments() {
        return _comments;
    }

    public void set_comments(int _comments) {
        this._comments = _comments;
    }
}
