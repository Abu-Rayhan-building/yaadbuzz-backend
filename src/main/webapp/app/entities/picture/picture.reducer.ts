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

import { IPicture, defaultValue } from 'app/shared/model/picture.model';

export const ACTION_TYPES = {
  FETCH_PICTURE_LIST: 'picture/FETCH_PICTURE_LIST',
  FETCH_PICTURE: 'picture/FETCH_PICTURE',
  CREATE_PICTURE: 'picture/CREATE_PICTURE',
  UPDATE_PICTURE: 'picture/UPDATE_PICTURE',
  DELETE_PICTURE: 'picture/DELETE_PICTURE',
  SET_BLOB: 'picture/SET_BLOB',
  RESET: 'picture/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPicture>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type PictureState = Readonly<typeof initialState>;

// Reducer

export default (state: PictureState = initialState, action): PictureState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PICTURE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PICTURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PICTURE):
    case REQUEST(ACTION_TYPES.UPDATE_PICTURE):
    case REQUEST(ACTION_TYPES.DELETE_PICTURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PICTURE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PICTURE):
    case FAILURE(ACTION_TYPES.CREATE_PICTURE):
    case FAILURE(ACTION_TYPES.UPDATE_PICTURE):
    case FAILURE(ACTION_TYPES.DELETE_PICTURE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PICTURE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_PICTURE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PICTURE):
    case SUCCESS(ACTION_TYPES.UPDATE_PICTURE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PICTURE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/pictures';

// Actions

export const getEntities: ICrudGetAllAction<IPicture> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PICTURE_LIST,
    payload: axios.get<IPicture>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IPicture> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PICTURE,
    payload: axios.get<IPicture>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPicture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PICTURE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IPicture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PICTURE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPicture> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PICTURE,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
