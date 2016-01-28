package rehabdata;

import java.sql.Timestamp;
import java.sql.Time;

/**
 * Created by esyundyukov on 18/01/16.
 */
public class RehabSession {

    // private variables
    int _id;
    Timestamp _startTime;
    Timestamp _endTime;
    int _movementAmount;
    double _movementAmplitude;
    double _avgFrequency;

    // Defining constructors
    public RehabSession() {}

    public RehabSession(int _id, Timestamp _startTime, Timestamp _endTime,
                        int _movementAmount, double _movementAmplitude, double _avgFrequency) {
        this._id = _id;
        this._startTime = _startTime;
        this._endTime = _endTime;
        this._movementAmount = _movementAmount;
        this._movementAmplitude = _movementAmplitude;
        this._avgFrequency = _avgFrequency;
    }

    public RehabSession(Timestamp _startTime, Timestamp _endTime,
                        int _movementAmount, double _movementAmplitude, double _avgFrequency) {
        this._startTime = _startTime;
        this._endTime = _endTime;
        this._movementAmount = _movementAmount;
        this._movementAmplitude = _movementAmplitude;
        this._avgFrequency = _avgFrequency;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Timestamp get_startTime() {
        return _startTime;
    }

    public void set_startTime(Timestamp _startTime) {
        this._startTime = _startTime;
    }

    public Timestamp get_endTime() {
        return _endTime;
    }

    public void set_endTime(Timestamp _endTime) {
        this._endTime = _endTime;
    }

    public int get_movementAmount() {
        return _movementAmount;
    }

    public void set_movementAmount(int _movementAmount) {
        this._movementAmount = _movementAmount;
    }

    public double get_movementAmplitude() {
        return _movementAmplitude;
    }

    public void set_movementAmplitude(double _movementAmplitude) {
        this._movementAmplitude = _movementAmplitude;
    }

    public double get_avgFrequency() {
        return _avgFrequency;
    }

    public void set_avgFrequency(double _avgFrequency) {
        this._avgFrequency = _avgFrequency;
    }
}
