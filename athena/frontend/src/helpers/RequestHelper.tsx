import axios, {AxiosRequestConfig} from "axios"

export default class RequestHelper {
  public static async post(url: string): Promise<boolean> {
    const options: AxiosRequestConfig = {}
    options.method = "POST"
    options.headers = {}

    const token = getCookie("XSRF-TOKEN")
    options.headers["X-XSRF-TOKEN"] = token
    return await RequestHelper.makeRequest(url, options)
  }

  public static postJson(url: string, jsonData: string): Promise<boolean> {
    const options: AxiosRequestConfig = {}
    options.method = "POST"
    options.headers = {}

    const token = getCookie("XSRF-TOKEN")
    options.headers["X-XSRF-TOKEN"] = token
    options.headers.Accept = "*/*"
    options.headers["Content-Type"] = "application/json"
    options.data = jsonData

    return RequestHelper.makeRequest(url, options)
  }

  public static postFile(url: string, formData: FormData): Promise<boolean> {
    const options: AxiosRequestConfig = {}
    options.method = "POST"
    options.headers = {}

    const token = getCookie("XSRF-TOKEN")
    options.headers["X-XSRF-TOKEN"] = token
    options.data = formData

    return RequestHelper.makeRequest(url, options)
  }

  public static async delete(url: string): Promise<boolean> {
    const options: AxiosRequestConfig = {}
    options.method = "DELETE"
    options.headers = {}

    const token = getCookie("XSRF-TOKEN")
    options.headers["X-XSRF-TOKEN"] = token

    return await RequestHelper.makeRequest(url, options)
  }

  public static async put(url: string, body: any): Promise<boolean> {
    const options: AxiosRequestConfig = {}
    options.method = "PUT"
    options.headers = {}

    const token = getCookie("XSRF-TOKEN")
    options.headers["X-XSRF-TOKEN"] = token
    options.headers["Content-Type"] = "application/json"
    options.data = JSON.stringify(body)

    return await RequestHelper.makeRequest(url, options)
  }

  public static get(url: string): Promise<Result> {
    return axios.get(url)
      .then((response) => {
        return {errorStatus: false, body: response.data}
      }).catch( () => {
        window.location.hash = "#/error"
        return {errorStatus: true, body: {}}
      })
  }

  private static makeRequest(url: string, options: any): Promise<boolean> {
    return axios.request({...options, url})
      .then(() => {
        return false
      }).catch( (anything) => {
        window.location.hash = "#/error"
        return true
      })
  }
}

export interface Result  {
  errorStatus: boolean,
  body: any
}

export function getCookie(tokenName: string): string {
  const name = `${tokenName}=`
  const cookieArray = document.cookie.split(";")

  return getTokenFromCookies(cookieArray, name)
}

function getTokenFromCookies(cookieArray: string[], name: string) {
  for (let cookie of cookieArray) {
    while (cookie.charAt(0) === " ") {
      cookie = cookie.substring(1)
    }
    if (cookie.indexOf(name) === 0) {
      return cookie.substring(name.length, cookie.length)
    }
  }
  return ""
}
