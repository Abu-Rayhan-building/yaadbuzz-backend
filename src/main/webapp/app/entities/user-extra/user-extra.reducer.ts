import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserExtra, defaultValue } from 'app/shared/model/user-extra.model';

export const ACTION_TYPES = {
  FETCH_USEREXTRA_LIST: 'userExtra/FETCH_USEREXTRA_LIST',
  FETCH_USEREXTRA: 'userExtra/FETCH_USEREXTRA',
  CREATE_USEREXTRA: 'userExtra/CREATE_USEREXTRA',
  UPDATE_USEREXTRA: 'userExtra/UPDATE_USEREXTRA',
  DELETE_USEREXTRA: 'userExtra/DELETE_USEREXTRA',
  RESET: 'userExtra/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserExtra>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type UserExtraState = Readonly<typeof initialState>;

// Reducer

export default (state: UserExtraState = initialState, action): UserExtraState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USEREXTRA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USEREXTRA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_USEREXTRA):
    case REQUEST(ACTION_TYPES.UPDATE_USEREXTRA):
    case REQUEST(ACTION_TYPES.DELETE_USEREXTRA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_USEREXTRA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USEREXTRA):
    case FAILURE(ACTION_TYPES.CREATE_USEREXTRA):
    case FAILURE(ACTION_TYPES.UPDATE_USEREXTRA):
    case FAILURE(ACTION_TYPES.DELETE_USEREXTRA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_USEREXTRA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_USEREXTRA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_USEREXTRA):
    case SUCCESS(ACTION_TYPES.UPDATE_USEREXTRA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_USEREXTRA):
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

const apiUrl = 'api/user-extras';

// Actions

export const getEntities: ICrudGetAllAction<IUserExtra> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USEREXTRA_LIST,
    payload: axios.get<IUserExtra>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IUserExtra> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USEREXTRA,
    payload: axios.get<IUserExtra>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IUserExtra> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USEREXTRA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserExtra> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USEREXTRA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserExtra> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USEREXTRA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
