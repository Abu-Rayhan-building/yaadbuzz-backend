import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITopic, defaultValue } from 'app/shared/model/topic.model';

export const ACTION_TYPES = {
  FETCH_TOPIC_LIST: 'topic/FETCH_TOPIC_LIST',
  FETCH_TOPIC: 'topic/FETCH_TOPIC',
  CREATE_TOPIC: 'topic/CREATE_TOPIC',
  UPDATE_TOPIC: 'topic/UPDATE_TOPIC',
  DELETE_TOPIC: 'topic/DELETE_TOPIC',
  RESET: 'topic/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITopic>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type TopicState = Readonly<typeof initialState>;

// Reducer

export default (state: TopicState = initialState, action): TopicState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TOPIC_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TOPIC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TOPIC):
    case REQUEST(ACTION_TYPES.UPDATE_TOPIC):
    case REQUEST(ACTION_TYPES.DELETE_TOPIC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TOPIC_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TOPIC):
    case FAILURE(ACTION_TYPES.CREATE_TOPIC):
    case FAILURE(ACTION_TYPES.UPDATE_TOPIC):
    case FAILURE(ACTION_TYPES.DELETE_TOPIC):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TOPIC_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TOPIC):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TOPIC):
    case SUCCESS(ACTION_TYPES.UPDATE_TOPIC):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TOPIC):
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

const apiUrl = 'api/topics';

// Actions

export const getEntities: ICrudGetAllAction<ITopic> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TOPIC_LIST,
  payload: axios.get<ITopic>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ITopic> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TOPIC,
    payload: axios.get<ITopic>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITopic> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TOPIC,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITopic> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TOPIC,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITopic> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TOPIC,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
