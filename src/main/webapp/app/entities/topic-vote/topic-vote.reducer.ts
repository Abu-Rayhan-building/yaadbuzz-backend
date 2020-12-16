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

import { ITopicVote, defaultValue } from 'app/shared/model/topic-vote.model';

export const ACTION_TYPES = {
  FETCH_TOPICVOTE_LIST: 'topicVote/FETCH_TOPICVOTE_LIST',
  FETCH_TOPICVOTE: 'topicVote/FETCH_TOPICVOTE',
  CREATE_TOPICVOTE: 'topicVote/CREATE_TOPICVOTE',
  UPDATE_TOPICVOTE: 'topicVote/UPDATE_TOPICVOTE',
  DELETE_TOPICVOTE: 'topicVote/DELETE_TOPICVOTE',
  RESET: 'topicVote/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITopicVote>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TopicVoteState = Readonly<typeof initialState>;

// Reducer

export default (state: TopicVoteState = initialState, action): TopicVoteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TOPICVOTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TOPICVOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TOPICVOTE):
    case REQUEST(ACTION_TYPES.UPDATE_TOPICVOTE):
    case REQUEST(ACTION_TYPES.DELETE_TOPICVOTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TOPICVOTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TOPICVOTE):
    case FAILURE(ACTION_TYPES.CREATE_TOPICVOTE):
    case FAILURE(ACTION_TYPES.UPDATE_TOPICVOTE):
    case FAILURE(ACTION_TYPES.DELETE_TOPICVOTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TOPICVOTE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_TOPICVOTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TOPICVOTE):
    case SUCCESS(ACTION_TYPES.UPDATE_TOPICVOTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TOPICVOTE):
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

const apiUrl = 'api/topic-votes';

// Actions

export const getEntities: ICrudGetAllAction<ITopicVote> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TOPICVOTE_LIST,
    payload: axios.get<ITopicVote>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITopicVote> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TOPICVOTE,
    payload: axios.get<ITopicVote>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITopicVote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TOPICVOTE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ITopicVote> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TOPICVOTE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITopicVote> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TOPICVOTE,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
