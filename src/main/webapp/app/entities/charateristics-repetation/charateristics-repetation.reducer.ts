import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICharateristicsRepetation, defaultValue } from 'app/shared/model/charateristics-repetation.model';

export const ACTION_TYPES = {
  FETCH_CHARATERISTICSREPETATION_LIST: 'charateristicsRepetation/FETCH_CHARATERISTICSREPETATION_LIST',
  FETCH_CHARATERISTICSREPETATION: 'charateristicsRepetation/FETCH_CHARATERISTICSREPETATION',
  CREATE_CHARATERISTICSREPETATION: 'charateristicsRepetation/CREATE_CHARATERISTICSREPETATION',
  UPDATE_CHARATERISTICSREPETATION: 'charateristicsRepetation/UPDATE_CHARATERISTICSREPETATION',
  DELETE_CHARATERISTICSREPETATION: 'charateristicsRepetation/DELETE_CHARATERISTICSREPETATION',
  RESET: 'charateristicsRepetation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICharateristicsRepetation>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type CharateristicsRepetationState = Readonly<typeof initialState>;

// Reducer

export default (state: CharateristicsRepetationState = initialState, action): CharateristicsRepetationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHARATERISTICSREPETATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHARATERISTICSREPETATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CHARATERISTICSREPETATION):
    case REQUEST(ACTION_TYPES.UPDATE_CHARATERISTICSREPETATION):
    case REQUEST(ACTION_TYPES.DELETE_CHARATERISTICSREPETATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CHARATERISTICSREPETATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHARATERISTICSREPETATION):
    case FAILURE(ACTION_TYPES.CREATE_CHARATERISTICSREPETATION):
    case FAILURE(ACTION_TYPES.UPDATE_CHARATERISTICSREPETATION):
    case FAILURE(ACTION_TYPES.DELETE_CHARATERISTICSREPETATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARATERISTICSREPETATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHARATERISTICSREPETATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHARATERISTICSREPETATION):
    case SUCCESS(ACTION_TYPES.UPDATE_CHARATERISTICSREPETATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHARATERISTICSREPETATION):
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

const apiUrl = 'api/charateristics-repetations';

// Actions

export const getEntities: ICrudGetAllAction<ICharateristicsRepetation> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CHARATERISTICSREPETATION_LIST,
  payload: axios.get<ICharateristicsRepetation>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ICharateristicsRepetation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHARATERISTICSREPETATION,
    payload: axios.get<ICharateristicsRepetation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICharateristicsRepetation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHARATERISTICSREPETATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICharateristicsRepetation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHARATERISTICSREPETATION,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICharateristicsRepetation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHARATERISTICSREPETATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
