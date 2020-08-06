import { SET_PATH } from '@/store/shared/mutationTypes'
import { SEARCH_PATH } from '@/store/shared/actionTypes'
import PathService from '@/api/modules/path'

const state = {
  pathResult: null
}

const getters = {
  pathResult(state) {
    return state.pathResult
  }
}

const mutations = {
  setPath(state, pathResult) {
    state.pathResult = pathResult
  }
}

const actions = {
  async searchPath({commit}, path) {
    return PathService.get(path).then(({ data }) => {
      commit('setPath', data)
    })
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
