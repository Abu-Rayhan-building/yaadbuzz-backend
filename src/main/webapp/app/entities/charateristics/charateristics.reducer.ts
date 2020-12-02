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

import { ICharateristics, defaultValue } from 'app/shared/model/charateristics.model';

export const ACTION_TYPES = {
  FETCH_CHARATERISTICS_LIST: 'charateristics/FETCH_CHARATERISTICS_LIST',
  FETCH_CHARATERISTICS: 'charateristics/FETCH_CHARATERISTICS',
  CREATE_CHARATERISTICS: 'charateristics/CREATE_CHARATERISTICS',
  UPDATE_CHARATERISTICS: 'charateristics/UPDATE_CHARATERISTICS',
  DELETE_CHARATERISTICS: 'charateristics/DELETE_CHARATERISTICS',
  RESET: 'charateristics/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICharateristics>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CharateristicsState = Readonly<typeof initialState>;

// Reducer

export default (state: CharateristicsState = initialState, action): CharateristicsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHARATERISTICS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHARATERISTICS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CHARATERISTICS):
    case REQUEST(ACTION_TYPES.UPDATE_CHARATERISTICS):
    case REQUEST(ACTION_TYPES.DELETE_CHARATERISTICS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CHARATERISTICS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHARATERISTICS):
    case FAILURE(ACTION_TYPES.CREATE_CHARATERISTICS):
    case FAILURE(ACTION_TYPES.UPDATE_CHARATERISTICS):
    case FAILURE(ACTION_TYPES.DELETE_CHARATERISTICS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARATERISTICS_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_CHARATERISTICS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHARATERISTICS):
    case SUCCESS(ACTION_TYPES.UPDATE_CHARATERISTICS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHARATERISTICS):
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

const apiUrl = 'api/charateristics';

// Actions

export const getEntities: ICrudGetAllAction<ICharateristics> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CHARATERISTICS_LIST,
    payload: axios.get<ICharateristics>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICharateristics> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHARATERISTICS,
    payload: axios.get<ICharateristics>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICharateristics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHARATERISTICS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ICharateristics> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHARATERISTICS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICharateristics> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHARATERISTICS,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
