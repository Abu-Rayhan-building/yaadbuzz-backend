import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './picture.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPictureDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PictureDetail = (props: IPictureDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { pictureEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pictureDetailsHeading">
          <Translate contentKey="yaadbuzzApp.picture.detail.title">Picture</Translate> [<strong>{pictureEntity.id}</strong>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="image">
              <Translate contentKey="yaadbuzzApp.picture.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {pictureEntity.address ? (
              <div>
                    <img src={`${process.env.SERVER_API_URL}api/picture/${pictureEntity.id}/file`} style={{ maxHeight: '30px' }} />
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.picture.comment">Comment</Translate>
          </dt>
        </dl>
        <Button tag={Link} to="/picture" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/picture/${pictureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ picture }: IRootState) => ({
  pictureEntity: picture.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PictureDetail);
