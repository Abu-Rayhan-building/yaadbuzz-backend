import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import userPerDepartment, {
  UserPerDepartmentState
} from 'app/entities/user-per-department/user-per-department.reducer';
// prettier-ignore
import department, {
  DepartmentState
} from 'app/entities/department/department.reducer';
// prettier-ignore
import memory, {
  MemoryState
} from 'app/entities/memory/memory.reducer';
// prettier-ignore
import comment, {
  CommentState
} from 'app/entities/comment/comment.reducer';
// prettier-ignore
import picture, {
  PictureState
} from 'app/entities/picture/picture.reducer';
// prettier-ignore
import topic, {
  TopicState
} from 'app/entities/topic/topic.reducer';
// prettier-ignore
import topicRating, {
  TopicRatingState
} from 'app/entities/topic-rating/topic-rating.reducer';
// prettier-ignore
import charateristics, {
  CharateristicsState
} from 'app/entities/charateristics/charateristics.reducer';
// prettier-ignore
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly userPerDepartment: UserPerDepartmentState;
  readonly department: DepartmentState;
  readonly memory: MemoryState;
  readonly comment: CommentState;
  readonly picture: PictureState;
  readonly topic: TopicState;
  readonly topicRating: TopicRatingState;
  readonly charateristics: CharateristicsState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  userPerDepartment,
  department,
  memory,
  comment,
  picture,
  topic,
  topicRating,
  charateristics,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
