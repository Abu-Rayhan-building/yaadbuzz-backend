import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMemory, defaultValue } from 'app/shared/model/memory.model';

export const ACTION_TYPES = {
  FETCH_MEMORY_LIST: 'memory/FETCH_MEMORY_LIST',
  FETCH_MEMORY: 'memory/FETCH_MEMORY',
  CREATE_MEMORY: 'memory/CREATE_MEMORY',
  UPDATE_MEMORY: 'memory/UPDATE_MEMORY',
  DELETE_MEMORY: 'memory/DELETE_MEMORY',
  RESET: 'memory/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMemory>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MemoryState = Readonly<typeof initialState>;

// Reducer

export default (state: MemoryState = initialState, action): MemoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEMORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEMORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MEMORY):
    case REQUEST(ACTION_TYPES.UPDATE_MEMORY):
    case REQUEST(ACTION_TYPES.DELETE_MEMORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MEMORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEMORY):
    case FAILURE(ACTION_TYPES.CREATE_MEMORY):
    case FAILURE(ACTION_TYPES.UPDATE_MEMORY):
    case FAILURE(ACTION_TYPES.DELETE_MEMORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMORY_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_MEMORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEMORY):
    case SUCCESS(ACTION_TYPES.UPDATE_MEMORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEMORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/memories';

// Actions

export const getEntities: ICrudGetAllAction<IMemory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MEMORY_LIST,
    payload: axios.get<IMemory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMemory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEMORY,
    payload: axios.get<IMemory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMemory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEMORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IMemory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEMORY,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMemory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEMORY,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
