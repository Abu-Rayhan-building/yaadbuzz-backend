import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMemoryPicture, defaultValue } from 'app/shared/model/memory-picture.model';

export const ACTION_TYPES = {
  FETCH_MEMORYPICTURE_LIST: 'memoryPicture/FETCH_MEMORYPICTURE_LIST',
  FETCH_MEMORYPICTURE: 'memoryPicture/FETCH_MEMORYPICTURE',
  CREATE_MEMORYPICTURE: 'memoryPicture/CREATE_MEMORYPICTURE',
  UPDATE_MEMORYPICTURE: 'memoryPicture/UPDATE_MEMORYPICTURE',
  DELETE_MEMORYPICTURE: 'memoryPicture/DELETE_MEMORYPICTURE',
  SET_BLOB: 'memoryPicture/SET_BLOB',
  RESET: 'memoryPicture/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMemoryPicture>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type MemoryPictureState = Readonly<typeof initialState>;

// Reducer

export default (state: MemoryPictureState = initialState, action): MemoryPictureState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEMORYPICTURE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEMORYPICTURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MEMORYPICTURE):
    case REQUEST(ACTION_TYPES.UPDATE_MEMORYPICTURE):
    case REQUEST(ACTION_TYPES.DELETE_MEMORYPICTURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MEMORYPICTURE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEMORYPICTURE):
    case FAILURE(ACTION_TYPES.CREATE_MEMORYPICTURE):
    case FAILURE(ACTION_TYPES.UPDATE_MEMORYPICTURE):
    case FAILURE(ACTION_TYPES.DELETE_MEMORYPICTURE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMORYPICTURE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEMORYPICTURE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEMORYPICTURE):
    case SUCCESS(ACTION_TYPES.UPDATE_MEMORYPICTURE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEMORYPICTURE):
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

const apiUrl = 'api/memory-pictures';

// Actions

export const getEntities: ICrudGetAllAction<IMemoryPicture> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEMORYPICTURE_LIST,
  payload: axios.get<IMemoryPicture>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IMemoryPicture> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEMORYPICTURE,
    payload: axios.get<IMemoryPicture>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMemoryPicture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEMORYPICTURE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMemoryPicture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEMORYPICTURE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMemoryPicture> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEMORYPICTURE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
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
