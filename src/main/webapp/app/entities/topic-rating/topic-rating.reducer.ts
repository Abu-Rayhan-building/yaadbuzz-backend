import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITopicRating, defaultValue } from 'app/shared/model/topic-rating.model';

export const ACTION_TYPES = {
  FETCH_TOPICRATING_LIST: 'topicRating/FETCH_TOPICRATING_LIST',
  FETCH_TOPICRATING: 'topicRating/FETCH_TOPICRATING',
  CREATE_TOPICRATING: 'topicRating/CREATE_TOPICRATING',
  UPDATE_TOPICRATING: 'topicRating/UPDATE_TOPICRATING',
  DELETE_TOPICRATING: 'topicRating/DELETE_TOPICRATING',
  RESET: 'topicRating/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITopicRating>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type TopicRatingState = Readonly<typeof initialState>;

// Reducer

export default (state: TopicRatingState = initialState, action): TopicRatingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TOPICRATING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TOPICRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TOPICRATING):
    case REQUEST(ACTION_TYPES.UPDATE_TOPICRATING):
    case REQUEST(ACTION_TYPES.DELETE_TOPICRATING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TOPICRATING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TOPICRATING):
    case FAILURE(ACTION_TYPES.CREATE_TOPICRATING):
    case FAILURE(ACTION_TYPES.UPDATE_TOPICRATING):
    case FAILURE(ACTION_TYPES.DELETE_TOPICRATING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TOPICRATING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TOPICRATING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TOPICRATING):
    case SUCCESS(ACTION_TYPES.UPDATE_TOPICRATING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TOPICRATING):
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

const apiUrl = 'api/topic-ratings';

// Actions

export const getEntities: ICrudGetAllAction<ITopicRating> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TOPICRATING_LIST,
  payload: axios.get<ITopicRating>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ITopicRating> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TOPICRATING,
    payload: axios.get<ITopicRating>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITopicRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TOPICRATING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITopicRating> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TOPICRATING,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITopicRating> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TOPICRATING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
