import axios from "axios"
import MockAdaptor from "axios-mock-adapter"
import RequestHelper, {getCookie, Result} from "../helpers/RequestHelper"
import {expect} from "../testHelpers/importsHelper"

describe("Request Helper", () => {
  let mock: MockAdaptor
  beforeEach(() => {
    mock = new MockAdaptor(axios)
  })
  afterEach(() => {
    mock.reset()
  })

  describe("postFile", () => {
    const options = {
      method: "POST",
      headers: {
        "X-XSRF-TOKEN": getCookie("XSRF-TOKEN")
      }
    }
    it("returns true when request fails", async () => {
      mock.onPost("/api/maintainers/upload").reply(400, options)

      const formData = new FormData()
      formData.append("file", "blah")

      const result = await RequestHelper.postFile("/api/maintainers/upload", formData)
      expect(result).to.be.true
      expect(mock.history.post.length).to.eq(1)
    })

    it("returns false when request works", async () => {
      mock.onPost("/api/maintainers/upload").reply(200, options)

      const formData = new FormData()
      formData.append("file", "blah")

      const result = await RequestHelper.postFile("/api/maintainers/upload", formData)
      expect(result).to.be.false
      expect(mock.history.post.length).to.be.eq(1)
    })
  })

  describe("post", () => {
    const options = {
      method: "POST",
      headers: {
        "X-XSRF-TOKEN": getCookie("XSRF-TOKEN")
      }
    }
    it("returns true when request fails", async () => {
      mock.onPost("/api/minerva").reply(404, options)

      const result = await RequestHelper.post("/api/minerva")

      expect(result).to.be.true
      expect(mock.history.post.length).to.be.eq(1)
    })

    it("returns false when request succeeds", async () => {
      mock.onPost("/api/minerva").reply(200, options)

      const result = await RequestHelper.post("/api/minerva")

      expect(result).to.be.false
      expect(mock.history.post.length).to.be.eq(1)
    })
  })

  describe("put", () => {
    const options = {
      method: "PUT",
      headers: {
        "X-XSRF-TOKEN": getCookie("XSRF-TOKEN"),
        "content-type": "application/json"
      }
    }

    it("returns false when request succeeds", async () => {
      mock.onPut("/api/maintainers").reply(200, options)

      const result = await RequestHelper.put("/api/maintainers", {})

      expect(result).to.be.false
      expect(mock.history.put.length).to.be.eq(1)
    })

    it("returns true when request fails", async () => {
      mock.onPut("/api/maintainers").reply(404, options)

      const result = await RequestHelper.put("/api/maintainers", {})

      expect(result).to.be.true
      expect(mock.history.put.length).to.be.eq(1)
    })

    it("Includes the body argument in the request", async () => {
      mock.onPut("/api/maintainers").reply(200, options)

      const body = {testVal: "blah"}
      await RequestHelper.put("/api/maintainers", body)
      const requestBody = mock.history.put[0].data
      expect(requestBody).to.eq(JSON.stringify(body))
    })
  })

  describe("get", () => {
    it("returns true when request fails", async () => {
      mock.onGet("/api/maintainers").reply(404)

      const result = await RequestHelper.get("/api/maintainers")

      expect(result.errorStatus).to.be.true
      expect(mock.history.get.length).to.eq(1)
    })

    it("returns false when request succeeds", async () => {
      const response = new Response("ok", {status: 200})
      mock.onGet("/api/maintainers").reply(200, response)

      const result: Result = await RequestHelper.get("/api/maintainers")

      expect(result.errorStatus).to.be.false
      expect(result.body).to.exist
      expect(mock.history.get.length).to.eq(1)
    })
  })

  describe("delete", () => {
    const options = {
      method: "DELETE",
      headers: {
        "X-XSRF-TOKEN": getCookie("XSRF-TOKEN")
      }
    }

    it("returns true when request fails", async () => {
      mock.onDelete("/api/absences/1").reply(404, options)

      const result = await RequestHelper.delete("/api/absences/1")

      expect(result).to.be.true
      expect(mock.history.delete.length).to.eq(1)
    })

    it("returns false when request succeeds", async () => {
      mock.onDelete("/api/absences/1").reply(200, options)

      const result = await RequestHelper.delete("/api/absences/1")

      expect(result).to.be.false
      expect(mock.history.delete.length).to.eq(1)
    })
  })
})
