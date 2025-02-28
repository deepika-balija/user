package com.apexon.catchIt.user.dto;


import com.apexon.catchIt.user.model.Roles;

public class AssignRolesDto {
        public Long getUserId() {
                return userId;
        }

        public void setUserId(Long userId) {
                this.userId = userId;
        }

        public Roles getRole() {
                return role;
        }

        public void setRole(Roles role) {
                this.role = role;
        }

        private Long userId;
        private Roles role;




}
