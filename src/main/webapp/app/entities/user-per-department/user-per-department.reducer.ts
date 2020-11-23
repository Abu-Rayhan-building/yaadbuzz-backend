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

import { IUserPerDepartment, defaultValue } from 'app/shared/model/user-per-department.model';

export const ACTION_TYPES = {
  FETCH_USERPERDEPARTMENT_LIST: 'userPerDepartment/FETCH_USERPERDEPARTMENT_LIST',
  FETCH_USERPERDEPARTMENT: 'userPerDepartment/FETCH_USERPERDEPARTMENT',
  CREATE_USERPERDEPARTMENT: 'userPerDepartment/CREATE_USERPERDEPARTMENT',
  UPDATE_USERPERDEPARTMENT: 'userPerDepartment/UPDATE_USERPERDEPARTMENT',
  DELETE_USERPERDEPARTMENT: 'userPerDepartment/DELETE_USERPERDEPARTMENT',
  RESET: 'userPerDepartment/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserPerDepartment>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type UserPerDepartmentState = Readonly<typeof initialState>;

// Reducer

export default (state: UserPerDepartmentState = initialState, action): UserPerDepartmentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERPERDEPARTMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERPERDEPARTMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_USERPERDEPARTMENT):
    case REQUEST(ACTION_TYPES.UPDATE_USERPERDEPARTMENT):
    case REQUEST(ACTION_TYPES.DELETE_USERPERDEPARTMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_USERPERDEPARTMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERPERDEPARTMENT):
    case FAILURE(ACTION_TYPES.CREATE_USERPERDEPARTMENT):
    case FAILURE(ACTION_TYPES.UPDATE_USERPERDEPARTMENT):
    case FAILURE(ACTION_TYPES.DELETE_USERPERDEPARTMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERPERDEPARTMENT_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_USERPERDEPARTMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERPERDEPARTMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_USERPERDEPARTMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERPERDEPARTMENT):
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

const apiUrl = 'api/user-per-departments';

// Actions

export const getEntities: ICrudGetAllAction<IUserPerDepartment> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERPERDEPARTMENT_LIST,
    payload: axios.get<IUserPerDepartment>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IUserPerDepartment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERPERDEPARTMENT,
    payload: axios.get<IUserPerDepartment>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IUserPerDepartment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERPERDEPARTMENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IUserPerDepartment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERPERDEPARTMENT,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserPerDepartment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERPERDEPARTMENT,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
