namespace Numerology.Core.Constants
{
    public static class UserRole
    {
        public const string ADMIN = "Admin";
        public const string MODERATOR = "Moderator";
        public const string USER = "User";
        public static readonly string[] ALL = [ADMIN, USER, MODERATOR];
    }
}