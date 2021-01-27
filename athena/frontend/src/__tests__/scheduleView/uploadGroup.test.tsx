import React from "react"
import UploadGroup from "../../components/scheduleView/UploadGroup"
import RequestHelper from "../../helpers/RequestHelper"
import { expect, fireEvent, render, sinon, wait} from "../../testHelpers/importsHelper"

describe("Upload Group", () => {
  const postStub = sinon.stub(RequestHelper, "postFile")
  const uploadStub = sinon.stub()

  afterEach( () => {
    uploadStub.resetHistory()
  })

  it("includes a file input", () => {
    const rendered = render(<UploadGroup getMaintainerList={uploadStub}/>)
    const container = rendered.container
    expect(container.querySelectorAll("input").length).to.eq(1)
    expect(container.innerHTML).to.not.contain("Successful Upload")
  })

  describe("uploadFile", () => {
    it(" makes a request with the passed in fileList", () => {
      const rendered = render(<UploadGroup getMaintainerList={uploadStub}/>)
      const container = rendered.container

      const file = new File([], "testFile")
      const formData = new FormData()
      formData.append("file", file)
      postStub.withArgs(`/api/maintainers`, formData)
        .returns(Promise.resolve(true))

      const fileInput = container.querySelector("#file-input")

      if (fileInput) {
        fireEvent.change(fileInput, {target: {files: [file]}})
      }

      expect(postStub.callCount).to.eq(1)
      postStub.reset()
    })
    it("displays success message on success", async () => {
      const {getByText, getByTestId} = render(<UploadGroup getMaintainerList={uploadStub}/>)

      const file = new File([], "testFile")
      const formData = new FormData()
      formData.append("file", file)
      postStub.returns(Promise.resolve(false))

      const fileInput = getByTestId("file-input")

      if (fileInput) {
        fireEvent.change(fileInput, {target: {files: [file]}})
      }

      expect(postStub.callCount).to.eq(1)
      await wait(() => getByText("Successful Upload"))
      expect(uploadStub).to.have.been.called
    })
  })
})
