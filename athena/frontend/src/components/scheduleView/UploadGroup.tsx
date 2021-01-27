import React from "react"
import RequestHelper from "../../helpers/RequestHelper"

interface State {
  uploadStatus: string
}

interface Props {
  getMaintainerList: () => void
}

export default class UploadGroup extends React.Component<Props, State> {

  private static upload() {
    const input = document.getElementById("file-input")

    if (input) {
      input.click()
    }
  }

  constructor(props: Props) {
    super(props)
    this.state = {
      uploadStatus: ""
    }
  }

  public render() {
    return (
      <div>
        <button className="upload-button" onClick={UploadGroup.upload}>Upload File</button>
        <input accept=".csv" id="file-input" data-testid="file-input" type="file" hidden={true} onChange={(e) => {
          if (e.target.files && e.target.files.length > 0) {
            this.uploadFile(e.target.files)
          }
        }}/>
        <div className="message-status">
          <div>{this.state.uploadStatus}</div>
        </div>
      </div>
    )
  }

  public async uploadFile(fileList: FileList) {
    const formData = new FormData()
    formData.append("file", fileList[0])
    const isError = await RequestHelper.postFile("/api/maintainers/upload", formData)
    if (!isError) {
      this.setState({uploadStatus: "Successful Upload"})
      this.props.getMaintainerList()
    }
  }

}
