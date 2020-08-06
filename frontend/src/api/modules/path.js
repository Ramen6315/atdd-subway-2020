import ApiService from '@/api'

const BASE_URL = '/paths'

const PathService = {
  get(path) {
    return ApiService.get(`${BASE_URL}?source=${path.source}&target=${path.target}&type=${path.type}`)
  }
}


export default PathService
