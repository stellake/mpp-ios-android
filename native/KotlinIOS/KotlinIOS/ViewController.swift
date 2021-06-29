import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        stations.count
    }
    
    @IBOutlet private var label: UILabel!
    @IBOutlet private var submitButton: UIButton!
    @IBOutlet private var departurePicker: UIPickerView!
    @IBOutlet private var arrivalPicker: UIPickerView!

    
    var stations:[String] = [String]()
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        self.departurePicker.delegate = self
        self.departurePicker.dataSource = self
        
        self.arrivalPicker.delegate = self
        self.arrivalPicker.dataSource = self
        
        stations = ["Edingburgh Waverley [EDB]", "Newcastle [NCL]", "York [YRK]", "Kings Cross St. Pancras [KGX]", "Leeds [LDS]"]
        
//        submitButton.addTarget(self, action : Selector("submitStations"), for : .touchUpInside);
        
    }
    
    override func didReceiveMemoryWarning(){
        super.didReceiveMemoryWarning()
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
            return stations[row]
        }
    
    @IBAction func submitStations(_ sender : AnyObject){
        let departurePicked = stations[departurePicker.selectedRow(inComponent: 0)].split(separator: " ").last!.replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "")
        let arrivalPicked = stations[arrivalPicker.selectedRow(inComponent: 0)].split(separator: " ").last!.replacingOccurrences(of: "[", with: "").replacingOccurrences(of: "]", with: "")
        
        
        if let url = URL(string: "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/"+departurePicked+"/"+arrivalPicked+"/") {
          UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    
    
}

extension ViewController: ApplicationContractView {
    func setLabel(text: String) {
        //label.text = text
    }
}
