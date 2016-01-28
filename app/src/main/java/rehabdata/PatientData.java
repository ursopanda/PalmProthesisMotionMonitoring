package rehabdata;

/**
 * Created by Emil on 26/01/16.
 */
public class PatientData {

    // Personal data
    int _id;
    String _name;
    String _surname;
    String _phoneNumber;
    String _diagnosis;

    // This one will be gathered from Facebook APi
//  String _username;
//  String _password;
//  String _email;
//  String _sex;

    // Data related to rehab session
    String _definedMovementAmount;
    String _definedAmplitude;
    String _definedMovementFrequency;
    int _definedRehabLength;

    public PatientData() {}

    public PatientData(int _id, String _name, String _surname, String _phoneNumber, String _diagnosis,
                       String _definedMovementAmount, String _definedAmplitude, int _definedRehabLength) {
        this._id = _id;
        this._name = _name;
        this._surname = _surname;
        this._phoneNumber = _phoneNumber;
        this._diagnosis = _diagnosis;
        this._definedMovementAmount = _definedMovementAmount;
        this._definedAmplitude = _definedAmplitude;
        this._definedRehabLength = _definedRehabLength;
    }

}
