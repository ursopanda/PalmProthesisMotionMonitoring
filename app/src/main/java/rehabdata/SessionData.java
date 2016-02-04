package rehabdata;

/**
 * Created by Emil on 04/02/16.
 * Class to describe data structure
 * of table for each Rehab Session
 */
public class SessionData {
    int _id;
    int _movementCount;
    int _maxAngle;
    int _averagePeriod;
    int _sessionStartTime;
    int _sessionLength;

    public SessionData() {}

    public SessionData(int _id, int _movementCount, int _maxAngle, int _averagePeriod, int _sessionLength, int _sessionStartTime) {
        this._id = _id;
        this._movementCount = _movementCount;
        this._maxAngle = _maxAngle;
        this._averagePeriod = _averagePeriod;
        this._sessionLength = _sessionLength;
        this._sessionStartTime = _sessionStartTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_movementCount() {
        return _movementCount;
    }

    public void set_movementCount(int _movementCount) {
        this._movementCount = _movementCount;
    }

    public int get_maxAngle() {
        return _maxAngle;
    }

    public void set_maxAngle(int _maxAngle) {
        this._maxAngle = _maxAngle;
    }

    public int get_averagePeriod() {
        return _averagePeriod;
    }

    public void set_averagePeriod(int _averagePeriod) {
        this._averagePeriod = _averagePeriod;
    }

    public int get_sessionStartTime() {
        return _sessionStartTime;
    }

    public void set_sessionStartTime(int _sessionStartTime) {
        this._sessionStartTime = _sessionStartTime;
    }

    public int get_sessionLength() {
        return _sessionLength;
    }

    public void set_sessionLength(int _sessionLength) {
        this._sessionLength = _sessionLength;
    }
}
